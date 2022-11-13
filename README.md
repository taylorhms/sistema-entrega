# sistema-entrega

Atividades para 1ª avaliação da disciplina de Aplicação de Banco de Dados.

## Geral

Este projeto representa um sistema de entregas, utilizando o banco H2 para o armazenamento de dados e JPA para a conexão.

## Como executar

Para executar, é necessário ter o banco H2 em execução.

Clique com o botão direito do mouse sobre o ícone do da aplicação H2 e escolha a opção **Create a new database**.

![criar-bd-01](https://user-images.githubusercontent.com/106670176/201446321-dfcfefe1-78d7-4e05-b387-847eae388690.png)

Após isso, preencha o campo **Database path** com o caminho `~/sistema-entrega` e os campos **Username**, **Password** e **Password confirmation** com `sa`.

![criar-bd-02](https://user-images.githubusercontent.com/106670176/201446340-88109ffe-f09e-44c0-915d-ba1684ea88a8.png)

Nesse momento, uma base de dados foi criada na sua pasta de usuário.

Entre na console H2 na opção **Generic H2 (Server)** e use os seguintes parâmetros de conexão:

* JDBC URL: `jdbc:h2:tcp://localhost/~/sistema-entrega`
* User Name: `sa`
* Password: `sa`

Use os scripts de criação das tabelas localizado no diretório *sql* deste projeto.

**Obs:** As tabelas não são criadas automaticamente pelo JPA.

Agora basta executar a classe `SistemaEntrega`, localizada no pacote *exec*, na IDE de sua preferência.

## Pacote dao

Contém as classes responsáveis pela conexão com o banco de dados, utilizando JPA.

### Interface DAO

Esta interface apresenta os principais métodos para conexão e manipulação de dados do banco.

```java
public interface DAO<T> {
    default public EntityManager getEntityManager() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("sistema-entrega");
        EntityManager entityManager = factory.createEntityManager();
        return entityManager;
    }

    T findById(int id);
    List<T> findAll();
    void insert(T t);
    void update(T t);
    void delete(T t);
    void close();
}
```

### Classes CidadeDAO e EntregaDAO

Implementam a interface `DAO` para fazer o mapeamento objeto-relacional das classes Cidade e Entrega, respectivamente.

Retorna uma linha da tabela Cidade.

```java
    @Override
    public Cidade findById(int id) {
        return entityManager.find(Cidade.class, id);
    }
```

Faz uma consulta e retorna todas as tuplas da tabela Cidade.

```java
    @Override
    public List<Cidade> findAll() {
        Query query = entityManager.createQuery("select c from Cidade c", Cidade.class);
        return query.getResultList();
    }
```

Insere uma nova cidade no banco.

```java
    @Override
    public void insert(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(t);
        transaction.commit();
    }
```

Atualiza os dados de uma cidade existente.

```java
    @Override
    public void update(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.merge(t);
        transaction.commit();
    }
```

Remove uuma linha da tabela Cidade.

```java
    @Override
    public void delete(Cidade t) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Cidade c = entityManager.find(Cidade.class, t.getId());
        entityManager.remove(c);
        transaction.commit();
    }
```

Fecha a conexão com o banco.

```java
    @Override
    public void close() {
        entityManager.close();
    }
```

## Pacote entidades

Neste pacote encontram-se as classes que representam as tabelas no banco de dados.

### Classe Cidade

Representa uma tupla na tabela Cidade.

Atributos e construtores.

```java
    @Id private int id;
    private String nome;
    @Column(name="valor_frete") private float valorFrete;
    
    public Cidade() { }
    
    public Cidade(int id, String nome, float frete) {
        super();
        this.id = id;
        this.nome = nome;
        this.valorFrete = frete;
    }
```

### Classe Entrega

Representa uma tupla na tabela Entrega.

Atributos e construtores.

```java
    @Id private int id;
    
    @Column(name="nome_cliente") private String nomeCliente;
    @Column(name="peso_carga")   private float pesoCarga;
    @Column(name="valor_pagar")  private float valorPagar;
    
    @Temporal(TemporalType.DATE)
    @Column(name="data_despacho") private Date dataDespacho;
    
    @ManyToOne
    @JoinColumn(name="id_cidade") private Cidade cidade;
    
    public Entrega() { }
    
    public Entrega(int id, String nomeCliente, float pesoCarga, float valorPagar, Date dataDespacho,
            Cidade cidade) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.pesoCarga = pesoCarga;
        this.valorPagar = valorPagar;
        this.dataDespacho = dataDespacho;
        this.cidade = cidade;
    }
```

## Pacote exec

Contém a classe executável do projeto.

### Classe SistemaEntrega

Classe executável utilizando a console.

O método `main` exibe uma lista de opções para o usuário e chama o método que corresponda à ação escolhida.

```java
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
```

Coleta os dados via linha de comando, cria um objeto `Entrega` com esses dados e adiciona ao banco.

```java
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
```

Percorre uma lista com todas as entregas na base de dados e exibe os dados na console.

```java
    private static void consultarEntrega() {
        EntregaDAO dao = new EntregaDAO();
        List<Entrega> entregas = dao.findAll();
        
        System.out.println("\nID    NOME                           VALOR A PAGAR   CIDADE         DATA DO DESPACHO\n" +
                "------------------------------------------------------------------------------------");
        for (Entrega e : entregas) {
            System.out.printf("%-5d %-30s R$ %-12.2f %-20s %s\n", e.getId(), e.getNomeCliente(), e.getValorPagar(), e.getCidade().getNome(), formatarData.format(e.getDataDespacho()) );
        }
    }
```

Busca pela entrega com a chave informada pelo usuário. Caso exista, exibe os dados da mesma, permitindo alterações e atualiza a linha correspondente no banco.

```java
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
```

Apaga uma entrega com a chave informada pelo usuário. O usuário visualiza os dados da entrega e confirma a exclusão para que a linha seja removida da tabela no banco.

```java
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
```

Solicita dados da cidade via console e insere a mesma no banco de dados.

```java
    private static void adicionarCidade() {
        int idCidade = Integer.parseInt( ler("ID da cidade: ") );
        String nomeCidade = ler("Nome da cidade: ").toUpperCase();
        float valorFrete = Float.parseFloat( ler("Valor do frete: R$ ") );
        
        Cidade c = new Cidade(idCidade, nomeCidade, valorFrete);
        CidadeDAO dao = new CidadeDAO();
        
        dao.insert(c);
        
        System.out.println("\nAdicionado com sucesso.");
    }
```

Exibe uma lista com os dados de todas as tabelas armazenadas.

```java
    private static void consultarCidade() {
        CidadeDAO dao = new CidadeDAO();
        List<Cidade> cidades = dao.findAll();
        
        System.out.println("\nID    NOME            FRETE\n-------------------------------");
        for (Cidade c : cidades) {
            System.out.printf("%-5d %-15s R$ %6.2f\n", c.getId(), c.getNome(), c.getValorFrete());
        }
    }
```

Busca pela id da cidade informada pelo usuário. Caso exista, exibe os dados, permite alterações e atualiza a linha correspondente na tabela Cidade.

```java
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
```

Solicita entrada de dados por linha de comando.

```java
    private static String ler(String mensagem) {
        System.out.print(mensagem);
        return s.nextLine();
    }
```

## Diretório META-INF

Diretório padrão de projetos JPA, com dados da conexão.

### Arquivo persistence.xml

Arquivo com dados de acesso e descrição da base de dados.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2" xmlns="http://java.sun.com/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
  <persistence-unit name="sistema-entrega" transaction-type="RESOURCE_LOCAL">
    <class>entidades.Cidade</class>
    <class>entidades.Entrega</class>
    <properties>
      <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/sistema-entrega"/>
      <property name="javax.persistence.jdbc.user" value="sa"/>
      <property name="javax.persistence.jdbc.password" value="sa"/>
      <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
      <property name="javax.persistence.schema-generation.database.action" value="none"/>
    </properties>
  </persistence-unit>
</persistence>
```

## Diretório sql

Diretório com os scripts de configuração das tabelas no banco.

### Arquivo creates.sql

Scripts para criação das tabelas.

```sql
CREATE TABLE Cidade (
    id                INT  PRIMARY KEY,
    nome              VARCHAR(40),
    valor_frete       NUMERIC(10,2)
);

CREATE TABLE Entrega (
    id                INT PRIMARY KEY,
    nome_cliente      VARCHAR(80),
    peso_carga        NUMERIC(10,2),
    id_cidade         INT REFERENCES CIDADE(id),
    valor_pagar       NUMERIC(10,2),
    data_despacho     DATE
);
```

### Arquivo inserts.sql

Scripts para insersão de dados no banco.

```sql
INSERT INTO Cidade VALUES (1, 'BELEM', 12.55);
INSERT INTO Cidade VALUES (2, 'MANAUS', 15.45);
INSERT INTO Cidade VALUES (3, 'SAO PAULO', 5.56);
INSERT INTO Cidade VALUES (4, 'BELO HORIZONTE', 14.84);
INSERT INTO Cidade VALUES (5, 'SALVADOR', 26.95);
INSERT INTO Cidade VALUES (6, 'RIO DE JANEIRO', 7.24);

INSERT INTO Entrega VALUES (1001, 'AUGUSTO SOUZA', 6.00, 3, 15.50, '2022-11-05');
INSERT INTO Entrega VALUES (1002, 'BERNARDETE ANDRADE', 8.00, 5, 23.40, '2022-11-09');
INSERT INTO Entrega VALUES (1003, 'CINTIA ROCHA', 12.00, 2, 45.90, '2022-11-09');
INSERT INTO Entrega VALUES (1004, 'DIOGO DOS SANTOS', 3.00, 6, 9.85, '2022-11-10');
INSERT INTO Entrega VALUES (1005, 'EDUARDO CUNHA', 9.00, 1, 36.15, '2022-11-10');
INSERT INTO Entrega VALUES (1006, 'FERNANDA SILVA', 16.00, 4, 57.00, '2022-11-11');
INSERT INTO Entrega VALUES (1007, 'GREGORIO ALMEIDA', 5.00, 3, 17.25, '2022-11-11');
```
