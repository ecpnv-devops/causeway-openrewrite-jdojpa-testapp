package domainapp.modules.simple.fixture;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import org.springframework.core.io.ClassPathResource;

import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import lombok.experimental.Accessors;

import domainapp.modules.simple.dom.so.SimpleObject;
import domainapp.modules.simple.dom.so.SimpleObjects;

@RequiredArgsConstructor
public enum SimpleObject_persona
implements Persona<SimpleObject, SimpleObject_persona.Builder> {

    FOO("Foo"),
    BAR("Bar"),
    BAZ("Baz"),
    FRODO("Frodo"),
    FROYO("Froyo"),
    FIZZ("Fizz"),
    BIP("Bip"),
    BOP("Bop"),
    BANG("Bang"),
    BOO("Boo"),
    LIGHTSABER("Lightsaber"),
    DT15("DT-15 Blaster pistol"),
    EL16("EL-16 blaster rifle "),
    BOWCASTER("Bowcaster"),
    THEFORCE("The Force");

    private final String name;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public SimpleObject findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(SimpleObjects.class).map(x -> x.findByNameExact(name)).orElse(null);
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<SimpleObject> {

        @Getter @Setter private SimpleObject_persona persona;

        @Override
        protected SimpleObject buildResult(final ExecutionContext ec) {

            val simpleObject = wrap(simpleObjects).create(persona.name);

            simpleObject.setLastCheckedIn(clockService.getClock().nowAsLocalDate().plusDays(fakeDataService.ints().between(-10, +10)));

            return simpleObject;
        }

        @SneakyThrows
        private byte[] toBytes(String fileName){
            InputStream inputStream = new ClassPathResource(fileName, getClass()).getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[16384];

            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();
        }

        // -- DEPENDENCIES

        @Inject SimpleObjects simpleObjects;
        @Inject ClockService clockService;
        @Inject FakeDataService fakeDataService;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<SimpleObject, SimpleObject_persona, Builder> {
        public PersistAll() {
            super(SimpleObject_persona.class);
        }
    }


}
