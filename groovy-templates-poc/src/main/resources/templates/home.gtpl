yieldUnescaped '<!DOCTYPE html>'
html(lang: 'en') {
    head {
        title('Groovy Template POC')
    }
    body {
        h1('Groovy Template Example')
        p(message)
        p("Today is: ${date}")

        h2('Items List')
        ul {
            items.each { item ->
                li(item)
            }
        }

        h2('Greet Form')
        form(action: '/greet', method: 'post') {
            label(for: 'name', 'Enter your name:')
            input(type: 'text', name: 'name', id: 'name', required: '')
            button(type: 'submit', 'Greet')
        }
    }
}

