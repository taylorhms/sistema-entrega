package entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Entrega {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public float getPesoCarga() {
		return pesoCarga;
	}

	public void setPesoCarga(float pesoCarga) {
		this.pesoCarga = pesoCarga;
	}

	public float getValorPagar() {
		return valorPagar;
	}

	public void setValorPagar(float valorPagar) {
		this.valorPagar = valorPagar;
	}

	public Date getDataDespacho() {
		return dataDespacho;
	}

	public void setDataDespacho(Date dataDespacho) {
		this.dataDespacho = dataDespacho;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

}
