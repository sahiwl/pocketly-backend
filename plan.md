```
src/
└── main/
    ├── java/
    │   └── live/sahiwl/pocketlybe/
    │       ├── config/           # App config (CORS, Security, JWT filters)
    │       ├── controller/       # REST controllers (UserController, NoteController, TagController)
    │       ├── dto/              # Request/Response objects (LoginRequest, NoteDTO)
    │       ├── service/          # Business logic (UserService, NoteService, AuthService)
    │       ├── repository/       # Spring Data JPA interfaces (UserRepository, NoteRepository)
    │       ├── model/            # Entity classes (User, Note, Tag, Relationship entities)
    │       ├── exception/        # Custom exceptions + GlobalExceptionHandler
    │       └── PocketlyBeApplication.java
    │
    └── resources/
        ├── application.properties    # Base config
        ├── env.properties            # DB creds, JWT secret (gitignored)
        ├── schema.sql (optional)     # DDLs if you want
        └── data.sql (optional)       # Seed data for quick testing
```

