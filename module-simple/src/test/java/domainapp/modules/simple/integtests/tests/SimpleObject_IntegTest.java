package domainapp.modules.simple.integtests.tests;

import java.sql.Timestamp;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import org.apache.isis.applib.services.title.TitleService;
import org.apache.isis.applib.services.wrapper.DisabledException;
import org.apache.isis.applib.services.wrapper.InvalidException;
import org.apache.isis.persistence.jdo.datanucleus5.jdosupport.mixins.Persistable_datanucleusIdLong;
import org.apache.isis.persistence.jdo.datanucleus5.jdosupport.mixins.Persistable_datanucleusVersionTimestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domainapp.modules.simple.dom.impl.SimpleObject;
import domainapp.modules.simple.fixture.SimpleObject_persona;
import domainapp.modules.simple.integtests.SimpleModuleIntegTestAbstract;

@Transactional
public class SimpleObject_IntegTest extends SimpleModuleIntegTestAbstract {

    SimpleObject simpleObject;

    @BeforeEach
    public void setUp() {
        // given
        simpleObject = fixtureScripts.runPersona(SimpleObject_persona.FOO);
    }

    public static class Name extends SimpleObject_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(simpleObject).getName();

            // then
            assertThat(name).isEqualTo(simpleObject.getName());
        }

        @Test
        public void not_editable() {

            // expect
            assertThrows(DisabledException.class, ()->{

                // when
                wrap(simpleObject).setName("new name");

            });
        }

    }

    public static class UpdateName extends SimpleObject_IntegTest {

        @Test
        public void can_be_updated_directly() {

            // when
            wrap(simpleObject).updateName("new name");
            transactionService.flushTransaction();

            // then
            assertThat(wrap(simpleObject).getName()).isEqualTo("new name");
        }

        @Test
        public void failsValidation() {

            // expect
            InvalidException cause = assertThrows(InvalidException.class, ()->{

                // when
                wrap(simpleObject).updateName("new name!");

            });

            // also expect
            assertThat(cause.getMessage(), containsString("Exclamation mark is not allowed."));

        }
    }


    public static class Title extends SimpleObject_IntegTest {

        @Inject
        TitleService titleService;

        @Test
        public void interpolatesName() {

            // given
            final String name = wrap(simpleObject).getName();

            // when
            final String title = titleService.titleOf(simpleObject);

            // then
            assertThat(title).isEqualTo("Object: " + name);
        }
    }

    public static class DataNucleusId extends SimpleObject_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Long id = mixin(Persistable_datanucleusIdLong.class, simpleObject).prop();

            // then
            assertThat(id).isGreaterThanOrEqualTo(0);
        }
    }

    public static class DataNucleusVersionTimestamp extends SimpleObject_IntegTest {

        @Test
        public void should_be_populated() {
            // when
            final Timestamp timestamp = mixin(Persistable_datanucleusVersionTimestamp.class, simpleObject).prop();
            // then
            assertThat(timestamp).isNotNull();
        }
    }

}