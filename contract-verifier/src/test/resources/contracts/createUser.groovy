import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should create a new user"
    request {
        method POST()
        url "/api/users"
        headers {
            contentType(applicationJson())
        }
        body(
            name: "Alice Johnson",
            email: "alice.johnson@example.com",
            active: true
        )
    }
    response {
        status CREATED()
        headers {
            contentType(applicationJson())
        }
        body(
            id: anyPositiveInt(),
            name: "Alice Johnson",
            email: "alice.johnson@example.com",
            active: true
        )
    }
}
