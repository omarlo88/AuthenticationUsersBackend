package omar.lo.sec;

public interface SecurityConstants {
    String SECRET = "omarlo88.omar@gmail.com";
    //Long EXPIRATION_TIME = (long) 864_000_000; //10 jours (10*24*3600)
    Long EXPIRATION_TIME = (long) 10*24*3600*1000;
    String TOKEN_PREFIX = "Bearer ";
    String JWT_HEADER_NAME = "Authorization";

}// SecurityConstants
