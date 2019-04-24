package omar.lo.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class UserForm {

    private String nom, prenom, email, username, password, confirmedPassword;

}// UserForm
