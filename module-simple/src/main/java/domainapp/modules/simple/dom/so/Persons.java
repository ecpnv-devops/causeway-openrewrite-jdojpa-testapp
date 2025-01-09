package domainapp.modules.simple.dom.so;

import java.util.List;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.query.Query;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jdo.applib.types.Email;

import lombok.RequiredArgsConstructor;

import domainapp.modules.simple.SimpleModule;
import domainapp.modules.simple.types.Name;

@Named(SimpleModule.NAMESPACE + ".Persons")
@DomainService
@Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class Persons {

    final RepositoryService repositoryService;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Person create(
            @Name final String firstName,
            @Parameter(optionality = Optionality.OPTIONAL) final String lastName,
            @Email final String email) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setEmail(email);
        return repositoryService.persist(p);
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    public Person findByEmail(final String email) {
        return repositoryService.firstMatch(
                        Query.named(Person.class, Person.NAMED_QUERY__FIND_BY_EMAIL)
                                .withParameter("email", email))
                .orElse(null);
    }

    @Action(semantics = SemanticsOf.SAFE)
    public List<Person> listAll() {
        return repositoryService.allInstances(Person.class);
    }
}
