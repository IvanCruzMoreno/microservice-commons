package com.ivanmoreno.commons.models.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"createAt", "preguntas", "asignatura"})
@AllArgsConstructor
@Entity
@Table(name = "examenes")
public class Examen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_at")
	private Date createAt;
	
	@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "examen"}, allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY, 
			   cascade = CascadeType.ALL,
			   orphanRemoval = true,
			   mappedBy = "examen")
	private List<Pregunta> preguntas;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asignatura_id", referencedColumnName = "id")
	@NotNull
	private Asignatura asignatura;
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	
	public Examen() {
		this.preguntas = new ArrayList<>();
	}
	
	public void setPreguntas(List<Pregunta> preguntas) {
		this.preguntas.clear();
		preguntas.forEach(this::addPregunta);
	}
	
	public void addPregunta(Pregunta pregunta) {
		this.preguntas.add(pregunta);
		pregunta.setExamen(this);
	}
	
	public void deletePregunta(Pregunta pregunta) {
		this.preguntas.remove(pregunta);
		pregunta.setExamen(null);
	}
	
}
