package st.fixture

import ratpack.groovy.test.embed.GroovyEmbeddedApp
import ratpack.test.exec.ExecHarness

class SpringSecCheckTokenStub {
	static ExecHarness harness = ExecHarness.harness()

	static getStub() {
		GroovyEmbeddedApp.of {
			handlers {
				all {
					println "Called Stub"
					next()
				}

				prefix("oauth") {
					post("check_token") {
						def body = harness.yield{ e -> request.body }.getValue().text
						def token = body.substring(body.indexOf("=") + 1)
						println "Check_token stub got " + token
						if(token == "fakeToken") {
							render """{"exp":1438842239,"uuid":"abcd","user_name":"beckje01","authorities":["ROLE_CONSOLE","ROLE_USER"],"client_id":"clientapp","scope":["mobile","read"]}"""
						} else{
							response.status(401)
							response.send()
						}
					}
				}

				all {
					println "Hit no handlers " + request.path
					response.status(404)
					response.send()
				}
			}
		}
	}

}
