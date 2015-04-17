package repository;

import com.ctlok.microservices.user.management.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Lawrence Cheung
 */
@Data
@ToString( callSuper = true )
@Document( collection = "user" )
public class UserForCucumber extends User {

    @Transient
    private String strRoles;

    @Override
    public Collection<String> getRoles() {
        if ( strRoles != null ) {
            setRoles( Arrays.asList( strRoles.split( "," ) ) );
        }
        return super.getRoles();
    }

}
