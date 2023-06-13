package com.tech.entites.abstracts;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class MedicalEmployee extends Employee implements Serializable {
}
