package domainapp.webapp.application.services.homepage;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;

@Collection
@CollectionLayout(
        defaultView = "table",
        paged = 200
)
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class HomePageViewModel_items {

    private final HomePageViewModel homePageViewModel;

    @MemberSupport
    public List<SimpleObjectItem> coll() {
        return homePageViewModel.getObjects().stream()
                .map(SimpleObjectItem::new)
                .collect(Collectors.toList());
    }

}
