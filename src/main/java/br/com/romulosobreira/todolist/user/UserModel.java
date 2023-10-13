package br.com.romulosobreira.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
// @Getter -> Apenas para os getters
// @Setter -> Apenas para os setters
// Isso pode ser feito em cima de cada atributo, assim conseguindo fazer apenas
// para os atributos desajados, caso não queira todos. Como mostra no exemplo do
// atributo name
@Entity(name = "users")
public class UserModel {

  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(unique = true)
  private String username;
  // @Getter
  // @Setter
  private String name;
  private String password;

  @CreationTimestamp
  private LocalDateTime createAt;
}
