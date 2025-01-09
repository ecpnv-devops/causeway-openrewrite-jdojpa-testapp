package domainapp.modules.simple.integtests.tests;

import java.util.List;

import javax.inject.Inject;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.commons.functional.Try;
import org.apache.causeway.persistence.jdo.spring.exceptions.JdoResourceFailureException;
import org.apache.causeway.testing.unittestsupport.applib.matchers.ThrowableMatchers;

import lombok.val;

import domainapp.modules.simple.dom.so.Person;
import domainapp.modules.simple.dom.so.Persons;
import domainapp.modules.simple.fixture.Person_persona;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;

@Transactional
public class Persons_IntegTest extends SimpleModuleIntegTestAbstract {

    @Inject
    Persons menu;

    @Nested
    public static class listAll extends Persons_IntegTest {

        @Test
        public void happyCase() {

            // given
            fixtureScripts.run(new Person_persona.PersistAll());
            transactionService.flushTransaction();

            // when
            final List<Person> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(Person_persona.values().length);
        }

        @Test
        public void whenNone() {

            // when
            final List<Person> all = wrap(menu).listAll();

            // then
            assertThat(all).hasSize(0);
        }
    }

    @Nested
    public static class create extends Persons_IntegTest {

        @Test
        public void happyCase() {

            wrap(menu).create("Rey", "Palpatine", "rey@resistance.org");

            // then
            final List<Person> all = wrap(menu).listAll();
            assertThat(all).hasSize(1);
        }

        @Test
        public void whenAlreadyExists() {

            // given
            fixtureScripts.runPersona(Person_persona.YODA);
            interactionService.nextInteraction();

            // we execute this in its own transaction so that it can be discarded
            Try<Void> attempt = transactionService.runTransactional(Propagation.REQUIRES_NEW, () -> {

                // expect
                Throwable cause = assertThrows(Throwable.class, () -> {
                    // when
                    wrap(menu).create("Yoda", "", "yoda@resistance.org");
                    transactionService.flushTransaction();
                });

                // also expect
                MatcherAssert.assertThat(cause,
                        ThrowableMatchers.causedBy(DuplicateKeyException.class));
            });


            // then
            assertThat(attempt.isFailure()).isTrue();
            val failureIfAny = attempt.getFailure();
            assertThat(failureIfAny).isPresent();
            assertThat(failureIfAny.get()).isInstanceOf(JdoResourceFailureException.class);
            assertThat(failureIfAny.get()).hasMessageContaining("rollback-only");

        }

    }

    @Inject protected InteractionService interactionService;
}
