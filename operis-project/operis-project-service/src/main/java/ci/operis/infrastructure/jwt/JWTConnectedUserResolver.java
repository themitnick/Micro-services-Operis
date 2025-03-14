package ci.operis.infrastructure.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JWTConnectedUserResolver {

    private final JwtParser jwtParser;
    public String extractConnectedUserEmail(String token) {
        JWTClaimsSet jwtClaimsSet = jwtParser.parse(token.substring(7));
        return jwtClaimsSet.getSubject();
    }
}
