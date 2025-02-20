package com.postgresql.ytdemo2.Config;

import com.postgresql.ytdemo2.AuthenticationProvider.CustomDaoAuthenticationProvider;
import com.postgresql.ytdemo2.AuthenticationSuccessHandler.CustomAuthenticationSuccessHandler;
import com.postgresql.ytdemo2.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class PetConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;


    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .logout(logout -> logout //usually we assign a variable with a reference type to the anonymous object (that is implementing the class method we want) so we know what functional interface to implement (as could be more than one functional interface), here the logout method call takes a lambda expression  from the syntax "->" which creates
                        .logoutUrl("/logout")// URL to trigger logout/.....this here is continuation from line 36.....a anonymous object(which is the object used as the logout parameter) which will implement the "Consumer" class because of the method "logout(Consumer<LogoutConfigurer> logoutCustomizer)" , the "Consumer<LogoutConfigurer>" tells Java the reference type!
                        .logoutSuccessUrl("/homePage")  // Redirect after logout
                        .permitAll()
                )
                .authenticationProvider(authenticationProvider())// object/bean is returned from below and goes into the argument here
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/addPet").hasAnyRole("ADMIN","SENIOR");//hasAuthority("ROLE_ADMIN");     When a request comes to an endpoint, SecurityFilterChain checks the User object (stored in the session)
                    auth.requestMatchers("/updateById").hasAnyRole("ADMIN","SENIOR");//hasAuthority("ROLE_ADMIN");
                    auth.requestMatchers("/deleteById").hasRole("SENIOR");//hasAuthority("ROLE_ADMIN");
                    auth.requestMatchers("/signUp").hasRole("SENIOR");
                    auth.requestMatchers("/deleteUser").hasRole("SENIOR");

                    auth.anyRequest().permitAll();
                })
                //.userDetailsService(userDetailsService)//after user has entered details on login form "userDetailsService()" method here tells Spring use this service to check their details whenever its needed.
                // Also this method (which is a method from the HttpSecurity class as well as "formLogin, logout etc" method code) tells Spring to use "session-based authentication", so it says "Use a login form for authentication(above)" and " Use this userDetailsService to check login details". Create and manage sessions automatically
                //by using formLogin without disabling sessions, you're telling Spring "use the "default session-based authentication".

                //1)apparently we go to the UsernamePasswordAuthenticationFilter class where the attemptAuthentication method is followed by other methods and then we go to this "formLogin" which is the first filter in this method that we go to first.
                //the formLogin() part of this code below tells spring to use "session based authentication" (so cant use a JWT tokens even though a CRSF token is created automatically but we disabled it above)
                //Also we can view the session id number as well as the csrf token id number if we want to by.....
               .formLogin(form -> form// we come to this SecurityFilterChain method and then here if we click on a restricted endpoint and user has not yet already logged on using the authorizeHttpRequests method.
                       .loginPage("/login")
                       .successHandler( customAuthenticationSuccessHandler)// "when login succeeds, call the onAuthenticationSuccess method on this object". Spring handles the actual calling of the method automatically, and this object is used for the parameter "Authentication authentication" in the onAuthentication method
                       // ****so basicly this method takes an object that implements AuthenticationSuccessHandler interface****

                       //this "failureHandler" method here  takes a object from a anonymous class that  implement the  "AuthenticationFailureHandler" interface on the  method "onAuthenticationFailure"

                       //this "failureHandler method call below along with "(Exception exception)" statement which comes before this failureHandler method call is in a "catch" block and the "authenticate" method is automatically called and is in a "try" block
                       //also this failureHandler method returns a object that we can use ".permitAll" method on
                       .failureHandler((request, response, exception) -> {
                           System.out.println("Authentication failed: " + exception.getClass().getName());
                           System.out.println("Error message: " + exception.getMessage());

                           // Handle different types of exceptions
                          if (exception instanceof AccountExpiredException) {
                              response.sendRedirect("/login?expiryDate");
                          }
                           else if (exception instanceof LockedException) {
                               response.sendRedirect("/login?hours");
                           }
                           else {
                               response.sendRedirect("/login?error");
                           }

                       })

                       .permitAll()//*** as can see "(request, response, exception)" from the "onAuthenticationFailure" method is focused on handling exceptions but you also have "request and "response"(which are automatically provided when we make a login request, so eg the request object contains all the login information (username, password, etc.)) objects to help manage failure response for when we do eg "response.sendRedirect("/login?badcreds");" where we can use for  "param.badcreds" in the login template


              )//we could have done ".failureHandler(customAuthenticationFailureHandler)" like we did with ".successHandler( customAuthenticationSuccessHandler)" instead of using a lambda expression

                .exceptionHandling(exceptionHandling -> exceptionHandling// spring automatically has a inbuilt wrong details exception where "error" shows up as a url endpoint where we use "param.error" to deal with that but this here is a unauthorised
                        .accessDeniedHandler((request, response, accessDeniedException) -> {// user exception which creates a endpoint "denied" and we used "param.denied" in the template as well
                            response.sendRedirect("/login?denied");
                        })

                );
        return http.build();



    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }//The @Bean annotation is the key here. When you mark a method with @Bean, Spring:1 Takes whatever object you return and 2 Stores it in its container and then Injects it wherever you use @Autowired or constructor injection with PasswordEncoder being the UserService class
    //The @Bean annotation is what tells Spring "take this object and save it for later use". Without @Bean, it would just be a method returning an object that goes nowhere!


    @Bean
    public AuthenticationProvider authenticationProvider() {
        CustomDaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder());// Call method above (line 81) directly to get the returned BCryptPasswordEncoder object instead of autowiring the object which is in the container "passwordEncoder" and then injected here,(we could  directly pass in a object "BCryptPasswordEncoder".
        provider.setUserDetailsService(userDetailsService);//basicly  cant autowire because you will have the @bean method and autowired in the same class which creates circular.....
        return provider;//when the securityFilterChain method is called spring uses this AuthenticationProvider object and uses the BCryptPasswordEncoder and userDetailsService object
    }
    //this provider object is used by spring, we dont use this bean/object
    // I am sure that spring automatically creates a authentication provider bean for us automatically if we dont create one like above



}

//the customauthenticationsuccesshandler is a more sophisticated version of the defaultsuccessurl method (this method is from the HttpSecurity class i think)
//.defaultSuccessUrl("/login", false), this here is very simple - just sends everyone to one URL, it can only specify one default URL
//and the false parameter just means "try to go to originally requested URL first" and it can't handle different roles differently and it can't add custom logic
// similar to ".failureUrl("/login?error")", only defaultSuccessUrl takes 2 arguments(2nd argument being a boolean) and failureUrl takes only one argument
//failureUrl is simpler but less flexible than failureHandler, as failureHandler method lets you handle different types of failures differently, while failureUrl sends all failures to the same URL.
// and similar can be said about successUrl and using customAuthenticationSuccessHandler which implements AuthenticationSuccessHandler interface














