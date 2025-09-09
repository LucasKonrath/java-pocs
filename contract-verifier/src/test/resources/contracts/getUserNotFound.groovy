import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return 404 when user not found"
    request {
        method GET()
        url "/api/users/999"
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status NOT_FOUND()
    }
}
