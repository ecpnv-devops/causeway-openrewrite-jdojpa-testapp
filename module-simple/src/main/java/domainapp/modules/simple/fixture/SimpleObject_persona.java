package domainapp.modules.simple.fixture;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.causeway.testing.fakedata.applib.services.Addresses;

import org.springframework.core.io.ClassPathResource;

import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.applib.value.Blob;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import domainapp.modules.simple.dom.cc.EmailAddress;

import domainapp.modules.simple.dom.cc.PostalAddress;

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

    FOO("Foo", "Foo.pdf"),
    BAR("Bar", "Bar.pdf"),
    BAZ("Baz", null),
    FRODO("Frodo", "Frodo.pdf"),
    FROYO("Froyo", null),
    FIZZ("Fizz", "Fizz.pdf"),
    BIP("Bip", null),
    BOP("Bop", null),
    BANG("Bang", "Bang.pdf"),
    BOO("Boo", null);

    private final String name;
    private final String contentFileName;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public SimpleObject findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(SimpleObjects.class).map(x -> x.findByNameExact(name)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<SimpleObject> {

        @Getter @Setter private SimpleObject_persona persona;

        @Override
        protected SimpleObject buildResult(final ExecutionContext ec) {

            val simpleObject = wrap(simpleObjects).create(persona.name);

            if (persona.contentFileName != null) {
                val bytes = toBytes(persona.contentFileName);
                val attachment = new Blob(persona.contentFileName, "application/pdf", bytes);
                simpleObject.updateAttachment(attachment);
            }

            simpleObject.setLastCheckedIn(clockService.getClock().nowAsLocalDate().plusDays(fakeDataService.ints().between(-10, +10)));

            fakeDataService.enums().anyOf(CommChannel.class).handle(simpleObject, serviceRegistry);

            return simpleObject;
        }

        static enum CommChannel {
            NONE {
                @Override
                void handle(SimpleObject simpleObject, ServiceRegistry serviceRegistry) {
                }
            },
            EMAIL{
                @Override
                void handle(SimpleObject simpleObject, ServiceRegistry serviceRegistry) {
                    val fakeDataService = serviceRegistry.lookupServiceElseFail(FakeDataService.class);
                    String firstName = fakeDataService.name().firstName();
                    String lastName = fakeDataService.name().lastName();
                    String emailAddr = firstName + "." + lastName + "@" + lastName + ".com";
                    EmailAddress sendTo = new EmailAddress();
                    sendTo.setEmailAddress(emailAddr);
                    simpleObject.setSendTo(sendTo);
                }
            },
            POSTAL{
                @Override
                void handle(SimpleObject simpleObject, ServiceRegistry serviceRegistry) {
                    val fakeDataService = serviceRegistry.lookupServiceElseFail(FakeDataService.class);
                    PostalAddress sendTo = new PostalAddress();
                    sendTo.setAddress1(fakeDataService.addresses().streetAddressNumber());
                    sendTo.setAddress2(fakeDataService.addresses().streetName());
                    sendTo.setCity(fakeDataService.addresses().city());
                    simpleObject.setSendTo(sendTo);
                }
            };

            abstract void handle(SimpleObject simpleObject, ServiceRegistry serviceRegistry);

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
