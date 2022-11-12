package exec;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dao.CidadeDAO;
import dao.EntregaDAO;
import entidades.Cidade;
import entidades.Entrega;

public class SistemaEntrega {
	public static Scanner s;
	public static SimpleDateFormat formatarData = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void main(String[] args) {
		java.util.Locale.setDefault(java.util.Locale.ENGLISH);
		s = new Scanner(java.lang.System.in);
		
		while (true) {
			System.out.print(" --- Sistema Entrega ---\n\n" +
                    "1) Adicionar entrega\n" +
                    "2) Consultar entrega\n" +
                    "3) Alterar entrega\n" +
                    "4) Remover entrega\n" +
                    "5) Adicionar cidade\n" +
                    "6) Consultar cidades\n" +
                    "7) Alterar cidade\n" +
                    "0) Sair\n\n" +
                    "Escolha uma opção: (0 - 6) ");
        
            String opcao = s.nextLine();
        
            try {
				switch (opcao) {
				    case "1": adicionarEntrega(); break;
				    case "2": consultarEntrega(); break;
				    case "3": alterarEntrega();   break;
				    case "4": removerEntrega();   break;
				    case "5": adicionarCidade();  break;
				    case "6": consultarCidade();  break;
				    case "7": alterarCidade();    break;
				    case "0": System.out.println("Finalizado com êxito."); System.exit(0);
				    default:  System.out.println("Opção inválida.\n");
				}
			} catch (ParseException e) {
				System.out.println("Data inválida.");
			} catch (Exception e) {
				System.out.println("Ocorreu um erro.");
			}
            
            System.out.print("\nEnter para continuar...");
            s.nextLine();
		}

	}

	private static void adicionarEntrega() throws ParseException {
		int idEntrega = Integer.parseInt( ler("ID da entrega: ") );
		String nomeCliente = ler("Nome do cliente: ").toUpperCase();
		float pesoCarga = Float.parseFloat( ler("Peso da carga: ") );
		float valorPagar = Float.parseFloat( ler("Valor a pagar: ") );
		Date dataDespacho = formatarData.parse( ler("Data do despacho: (dd/mm/aaaa) ") );
		int idCidade = Integer.parseInt( ler("ID da cidade: ") );
		
		CidadeDAO cDao = new CidadeDAO();
		Cidade c = cDao.findById(idCidade);
		
		if (c == null) {
            System.out.println("Cidade não encontrada.");
            return;
        }
		
		System.out.println("Nome da Cidade: " + c.getNome());
		
		EntregaDAO eDao = new EntregaDAO();
		Entrega e = new Entrega(idEntrega, nomeCliente, pesoCarga, valorPagar, dataDespacho, c);
		
		eDao.insert(e);
		
		System.out.println("\nAdicionado com sucesso.");
	}

	private static void consultarEntrega() {
		EntregaDAO dao = new EntregaDAO();
		List<Entrega> entregas = dao.findAll();
		
		System.out.println("\nID    NOME                           VALOR A PAGAR   CIDADE         DATA DO DESPACHO\n" +
				"------------------------------------------------------------------------------------");
		for (Entrega e : entregas) {
			System.out.printf("%-5d %-30s R$ %-12.2f %-20s %s\n", e.getId(), e.getNomeCliente(), e.getValorPagar(), e.getCidade().getNome(), formatarData.format(e.getDataDespacho()) );
		}
	}

	private static void alterarEntrega() throws ParseException {
		int idEntrega = Integer.parseInt( ler("ID da entrega: ") );
		EntregaDAO eDao = new EntregaDAO();
		Entrega e = eDao.findById(idEntrega);
		
		if (e == null) {
			System.out.println("Entrega não encontrada.");
			return;
		}
		
		System.out.println("\nEntrega " + e.getId());
		
		String nomeCliente = ler("Nome do cliente: " + e.getNomeCliente() + "\nNovo nome: (enter = manter) ").toUpperCase();
		String pesoCarga =ler("Peso da carga: " + e.getPesoCarga() + "\nNovo peso: (enter = manter) ");
		String valorPagar = ler("Valor a pagar: " + e.getValorPagar() + "\nNovo valor: (enter = manter) ");
		String dataDespacho = ler("Data do despacho: " + formatarData.format(e.getDataDespacho()) + "\nNova data: (enter = manter) ");
		String idCidade = ler("Cidade: " + e.getCidade().getNome() + "\nID da nova cidade: (enter = manter) ");
		
		if (!nomeCliente.isBlank()) {
			e.setNomeCliente(nomeCliente);
		}
		
		if (!pesoCarga.isBlank()) {
			e.setPesoCarga(Float.parseFloat(pesoCarga));
		}
		
		if (!valorPagar.isBlank()) {
			e.setValorPagar(Float.parseFloat(valorPagar));
		}
		
		if (!dataDespacho.isBlank()) {
			e.setDataDespacho(formatarData.parse(dataDespacho));
		}
		
		if (!idCidade.isBlank()) {
			CidadeDAO cDao = new CidadeDAO();
			Cidade c = cDao.findById(Integer.parseInt(idCidade));
			
			if (c == null) {
	            System.out.println("Cidade não encontrada.");
	            return;
	        }
			
			System.out.println("Nome da Cidade: " + c.getNome());
			e.setCidade(c);
		}
		
		eDao.update(e);
		
		System.out.println("\nAlterado com sucesso.");
	}

	private static void removerEntrega() {
		int idEntrega = Integer.parseInt( ler("ID da entrega: ") );
		EntregaDAO dao = new EntregaDAO();
		Entrega e = dao.findById(idEntrega);
		
		if (e == null) {
			System.out.println("Entrega não encontrada.");
			return;
		}
		
		System.out.println("\nEntrega " + e.getId() +
				"\n Cliente: " + e.getNomeCliente() +
				"\n Valor: " + e.getValorPagar() +
				"\n Data do despacho: " + formatarData.format(e.getDataDespacho()) );
		
		if (ler("\nDeseja apagar este item? (s/n) ").toLowerCase().equals("s")) {
			dao.delete(e);
			System.out.println("\nRemovido com sucesso.");
		} else {
			System.out.println("\nCancelado.");
		}
	}

	private static void adicionarCidade() {
		int idCidade = Integer.parseInt( ler("ID da cidade: ") );
		String nomeCidade = ler("Nome da cidade: ").toUpperCase();
		float valorFrete = Float.parseFloat( ler("Valor do frete: R$ ") );
		
		Cidade c = new Cidade(idCidade, nomeCidade, valorFrete);
		CidadeDAO dao = new CidadeDAO();
		
		dao.insert(c);
		
		System.out.println("\nAdicionado com sucesso.");
	}

	private static void consultarCidade() {
		CidadeDAO dao = new CidadeDAO();
		List<Cidade> cidades = dao.findAll();
		
		System.out.println("\nID    NOME            FRETE\n-------------------------------");
		for (Cidade c : cidades) {
			System.out.printf("%-5d %-15s R$ %6.2f\n", c.getId(), c.getNome(), c.getValorFrete());
		}
	}
	
	private static void alterarCidade() {
		int idCidade = Integer.parseInt( ler("ID da cidade: ") );
		CidadeDAO dao = new CidadeDAO();
		Cidade c = dao.findById(idCidade);
		
		if (c == null) {
            System.out.println("Cidade não encontrada.");
            return;
        }
		
		String nomeCidade = ler("Nome da cidade: " + c.getNome() + "\nNovo nome: (enter = manter) ").toUpperCase();
		String valorFrete = ler("Valor do frete: R$ " + c.getValorFrete() + "\nNovo valor: (enter = manter) ");
		
		if (!nomeCidade.isBlank()) {
			c.setNome(nomeCidade);
		}
		
		if (!valorFrete.isBlank()) {
			c.setValorFrete(Float.parseFloat(valorFrete));
		}
		
		dao.update(c);
		
		System.out.println("\nAlterado com sucesso.");
	}

	private static String ler(String mensagem) {
		System.out.print(mensagem);
		return s.nextLine();
	}

}
