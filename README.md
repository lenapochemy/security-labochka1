# Лабораторная работа №1 "Разработка защищенного REST API с интеграцией в CI/CD"
## Русакова Елена, группа P3417

### Описание проекта:
Информационная система для хранения коллекции Игрушек мишек


### Описание API:

- `POST /auth/login` - метод для аутентификации пользователя, принимает логин и пароль
- `POST /auth/reg` - метод для регистрации пользователя, принимает логин и пароль
- `GET /api/bears` - метод для получения списка всех мишек, этот метод доступен только для аутентифицированных пользователей
- `POST /api/bears` - метод для создания и сохранения нового мишки в коллекции, этот метод доступен только для аутентифицированных пользователей, принимает имя и материал мишки

## Реализованные меры защиты:
### Защита от SQL-инъекций
Используется ORM Hibernate, тем самым избегается работа с SQL запросами и не используется конкатенация строк

### Защита от XSS
Пользовательский ввод экранируется с помощью функции `HtmlUtils.htmlEscape`, исключается возможность внедрения кода
```java
    @PostMapping
    public BearToy addBear(@RequestBody BearToyDTO dto){
        BearToy bear = new BearToy(HtmlUtils.htmlEscape(dto.getName()), HtmlUtils.htmlEscape(dto.getMaterial()));
        return bearToyService.addBear(bear);
    }
```

### Хэширование паролей
Пароли хэшируются с помощью алгоритма bcrypt, а не хранятся в открытом виде, тем самым избегается утечка открытых паролей

```java
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public User registration(UserDTO dto){
        User user = new User(dto.getLogin(), passwordEncoder.encode(dto.getPassword()));...
    }
```
### Аутентификация по JWT-токенам
При успешном входе в систему пользователь получает jwt токен.
```java
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        User user = userService.login(userDTO);
        if (user != null) {
            return ResponseEntity.ok(userService.generateToken(user));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
```

Этот токен необходим для доступа к методам на /api/bears, его проверка реализована внутри Spring Security

```java
    @Bean
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/auth/login", "/auth/reg").permitAll()
                        .anyRequest().authenticated())
    ... 
}
```

## Отчеты security-сканеров:

### SAST (Static Application Security Testing) - spotbugs
![](/screenshots/spotbugs.png)

### SCA (Software Composition Analysis) - Snyk
![](/screenshots/snyk.png)
