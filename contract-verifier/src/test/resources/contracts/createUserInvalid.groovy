import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return bad request when creating user with invalid data"
    request {
        method POST()
        url "/api/users"
        headers {
            contentType(applicationJson())
        }
        body(
            name: "",
            email: "invalid@example.com",
            active: true
        )
    }
    response {
        status BAD_REQUEST()
    }
}
