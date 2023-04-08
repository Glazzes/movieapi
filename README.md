## Movie API
[![QA](https://github.com/Glazzes/movieapi/actions/workflows/code-quality.yaml/badge.svg)](https://github.com/Glazzes/movieapi/actions/workflows/code-quality.yaml)
[![Test](https://github.com/Glazzes/movieapi/actions/workflows/test.yaml/badge.svg)](https://github.com/Glazzes/movieapi/actions/workflows/code-quality.yaml)

### Goals
This is a simple api where I intend to practice many
"non code specific" related practices, such as the
following:

- Testing
- Caching
- API documentation with [SpringDoc](https://springdoc.org/)
- Coding quality standards and best practices with [checkstyle](https://checkstyle.org/) and [pmd](https://pmd.github.io/)
- CI/CD with GitHub actions

### How to build
Ensure you have docker installed on your machine, then run:

### Project
MovieAPI is a simple where a user can:
- register actors and movies
- associate movies and actors
- rate movies

```bash
docker compose up -d
```
