package domainapp.modules.simple.integtests.tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.transaction.annotation.Transactional;

import org.apache.causeway.applib.services.wrapper.DisabledException;

import domainapp.modules.simple.dom.so.Person;
import domainapp.modules.simple.fixture.Person_persona;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;

@Transactional
public class Person_IntegTest extends SimpleModuleIntegTestAbstract {

    Person person;

    @BeforeEach
    public void setUp() {
        // given
        person = fixtureScripts.runPersona(Person_persona.LUKE);
    }


    @Nested
    public static class firstname extends Person_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(person).getFirstName();

            // then
            assertThat(name).isEqualTo(person.getFirstName());
        }

        @Test
        public void not_editable() {

            // expect
            assertThrows(DisabledException.class, () -> {

                // when
                wrap(person).setFirstName("new name");
            });
        }

    }
}
