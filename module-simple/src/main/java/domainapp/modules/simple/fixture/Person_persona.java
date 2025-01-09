package domainapp.modules.simple.fixture;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;

import domainapp.modules.simple.dom.so.Person;
import domainapp.modules.simple.dom.so.Persons;
import domainapp.modules.simple.dom.so.SimpleObject;

import static domainapp.modules.simple.fixture.SimpleObject_persona.BOWCASTER;
import static domainapp.modules.simple.fixture.SimpleObject_persona.DT15;
import static domainapp.modules.simple.fixture.SimpleObject_persona.EL16;
import static domainapp.modules.simple.fixture.SimpleObject_persona.LIGHTSABER;
import static domainapp.modules.simple.fixture.SimpleObject_persona.THEFORCE;

@RequiredArgsConstructor
public enum Person_persona
        implements Persona<Person, Person_persona.Builder> {

    LUKE("Luke", "Skywalker", "luke.skywalker@resistance.org", THEFORCE, LIGHTSABER, DT15),
    LEIA("Leia", "Skywalker", "leia.skywalker@resistance.org", DT15),
    CHEWIE("Chewie", "Chewbacca", "chewbacca@resistance.org", BOWCASTER, LIGHTSABER, EL16),
    HAN("Han", "Solo", "han.solo@resistance.org", DT15, EL16),
    YODA("Yoda", "", "yoda@resistance.org", THEFORCE, LIGHTSABER),
    ;

    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<SimpleObject_persona> simpleObjectPersonas;

    Person_persona(String firstName, String lastName, String email, SimpleObject_persona... simpleObjectPersonas) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.simpleObjectPersonas = Arrays.asList(simpleObjectPersonas);
    }

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public Person findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(Persons.class).map(x -> x.findByEmail(email)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Person> {

        @Getter @Setter private Person_persona persona;

        @Override
        protected Person buildResult(final ExecutionContext ec) {
            val person = wrap(persons).create(persona.firstName, persona.lastName, persona.email);
            persona.simpleObjectPersonas.stream()
                    .map(sop -> {
                        SimpleObject so = sop.findUsing(serviceRegistry);
                        if (so == null) so = sop.build(this, ec);
                        return so;
                    })
                    .forEach(person::addSimpleObject);
            return person;
        }

        @Inject Persons persons;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Person, Person_persona, Builder> {
        public PersistAll() {
            super(Person_persona.class);
        }
    }


}
