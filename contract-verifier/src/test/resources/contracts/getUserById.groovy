import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return user by id"
    request {
        method GET()
        url "/api/users/1"
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status OK()
        headers {
            contentType(applicationJson())
        }
        body(
            id: 1,
            name: "John Doe",
            email: "john.doe@example.com",
            active: true
        )
    }
}
