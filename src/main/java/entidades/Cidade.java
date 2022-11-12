package entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cidade {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(float frete) {
		this.valorFrete = frete;
	}
    
}
