### Sring Security
- Spring Security는 Spring 기반의 애플리케이션의 보안(인증과 권한, 인가 등)을 담당하는 스프링 하위 프레임워크
- Spring Security는 '인증'과 '권한'에 대한 부분을 Filter 흐름에 따라 처리

### 
```
// SecurityFilterChain을 구현한 Configuration 클래스

// 기본
Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter <- SecurityContext 생성
  HeaderWriterFilter
  LogoutFilter 
  JwtAuthenticationFilter  <- addFilterBefore UsernamePasswordAuthenticationFilter.class
  LogFilter <- addFilterBefore BasicAuthenticationFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]

// .oauth2Login() 필터 설정 적용 시
Security filter chain: [
  DisableEncodeUrlFilter
  WebAsyncManagerIntegrationFilter
  SecurityContextPersistenceFilter
  HeaderWriterFilter
  LogoutFilter
  OAuth2AuthorizationRequestRedirectFilter  <- 추가
  OAuth2LoginAuthenticationFilter <- 추가
  JwtAuthenticationFilter
  LogFilter
  RequestCacheAwareFilter
  SecurityContextHolderAwareRequestFilter
  AnonymousAuthenticationFilter
  SessionManagementFilter
  ExceptionTranslationFilter
  FilterSecurityInterceptor
]
```

### 스프링 시큐리티 필터 동작 과정
<image src="https://blog.kakaocdn.net/dn/cXYbiA/btrzkpsUfoo/bz3aVvcB1uKdcRamU8BXM1/img.png">

### DelegatingFilterProxy
- 사용자의 요청이 들어오면 가장 먼저 DelegatingFilterProxy가 요청을 받아 FilterChainProxy에게 요청을 위임
- 이때 springSecurityFilterChain"이라는 이름을 가진 빈을 찾는데 그 빈이 FilterChainProxy

### WebSecurity
- 스프링 빈으로 등록한 SecurityFilterChain의 정보를 WebSecurity 클래스에게 전달
- WebSecurity는 각 설정 클래스로부터 필터 목록을 전달받아 FilterChainProxy의 생성자로 filter를 전달

### SecurityContextPersistenceFilter
- 내부적으로 HttpSessionSecurityContextRepository 클래스를 가진다
- HttpSessionSecurityContextRepository
  - 처음인증하거나 익명 사용자일 때
    - SecurityContext을 생성, SecurityContextHolder안에 저장을 하고 다음 필터를 실행
  - 세션에 저장된 SecurityContext가 있을 경우
    - SecurityContext를 꺼내와서 SecurityContextHolder에 저장
   
### UsernamePasswordAuthenticationFilter
- 인증 객체를 만들어서 Authentication 객체를 만들어 아이디 패스워드를 저장, AuthenticationManager에게 인증처리를 맡깁니다.
- AuthenticationManager는 AuthenticationProvider에게 인증 처리를 위임
- AuthenticationProvider는 UserDetailsService와 같은 서비스를 사용해서 인증을 검증
- 최종적으로 인증에 성공하면 Authentication 객체를 생성하고 SecurityContext에 저장

### ExceptionTranslationFilter
- 인증과 인가에 대해 각각 AccessDeniedException, AuthenticationException를 던진다

### FilterSecurityInterceptor
- Authentication 객체가 있는지 검사하고 없다면 exception을 날림
- 접근하고자 하는 리소스의 승인과 거부를 판단

### 나의 생각?
- SecurityContextPersistenceFilter의 HttpSessionSecurityContextRepository에서 생성한 SecurtiyContext
- 커스텀으로 구현한 JwtAuthenticationFilter에서 인증을 수행
- SecurityContextHolder.getContext().setAuthentication()을 통해 Authentication 객체를 넣어줌
- 이후 필터에서는 SecurtiyContext에 있는 AUthentication을 통해 인증 과정 수행, 인가 처리? (UsernamePasswordAuthentication ... )

출처 : [https://gngsn.tistory.com/160]


