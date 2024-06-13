package domainapp.webapp.application.services.homepage;

import domainapp.modules.simple.SimpleModule;

import domainapp.modules.simple.dom.cc.CommunicationChannel;
import domainapp.modules.simple.dom.so.SimpleObject;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.services.title.TitleService;

@Named(SimpleModule.NAMESPACE + ".SimpleObjectItem")
@DomainObject(nature = Nature.VIEW_MODEL)
@DomainObjectLayout
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleObjectItem {


    @Builder
    public SimpleObjectItem(
            final SimpleObject simpleObject) {
        this.simpleObject = simpleObject;
    }

    @Property
    @PropertyLayout(sequence = "1")
    @Getter @Setter
    private SimpleObject simpleObject;

    @ObjectSupport
    public String title() {
        return titleService.titleOf(getSimpleObject());
    }

    @Property()
    @PropertyLayout(sequence = "1.4")
    public CommunicationChannel getSendTo() {
        return getSimpleObject().getSendTo();
    }

    @Inject TitleService titleService;

}
