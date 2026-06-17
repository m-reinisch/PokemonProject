package de.mreinisch.pokemonproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/** Data transfer object for updating
 *
 * @param nickname
 */
public record UpdateDTO(
        @NotEmpty(message= "Nickname must not be empty.")
        @NotBlank(message= "Nickname must not be blank.")
        @Size(min= 3, message= "The nickname must contain at least 3 characters.")
        String nickname) {
}
