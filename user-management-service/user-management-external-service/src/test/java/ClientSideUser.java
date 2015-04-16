import com.ctlok.microservices.user.management.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Lawrence Cheung
 */
public class ClientSideUser extends User {

    @Override
    @JsonIgnore( false )
    public String getPassword() {
        return super.getPassword();
    }

}
