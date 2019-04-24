package omar.lo.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import omar.lo.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /* Le Autowired ne marche pas ici par cette classe n'a pas d'annotation spring */

    private ObjectMapper objectMapper;
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //String username = request.getParameter("username");
        //String password = request.getParameter("password");
        try {
            AppUser appUser = objectMapper.readValue(request.getInputStream(), AppUser.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
        } catch (IOException e) {
            System.out.println("JWTAuthenticationFilter.attemptAuthentication(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        User userSpring = (User) authResult.getPrincipal();
        List<String> roles = new ArrayList<>();
        /*authResult.getAuthorities().forEach(auth -> {
            roles.add(auth.getAuthority());
        });*/

        authResult.getAuthorities().forEach(auth -> roles.add(auth.getAuthority()));

        String jwt = JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(userSpring.getUsername())
                .withArrayClaim("roles", roles.toArray(new String[roles.size()]))
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(SecurityConstants.SECRET));
        response.addHeader(SecurityConstants.JWT_HEADER_NAME, jwt);
    }

}// JWTAuthenticationFilter
