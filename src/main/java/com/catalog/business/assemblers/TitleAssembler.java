package com.catalog.business.assemblers;

import com.catalog.business.dto.TitleDto;
import com.catalog.model.Title;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Igor on 1/8/2021.
 */
@Component
public class TitleAssembler {

    public TitleDto assmebleTitleDtoFromTitleEntity( Title title ){
        return new TitleDto( title.getId(), title.getRawName(), title.getImdbTitle(), title.getGenre(), title.getYear(), title.getIMDBlink(), title.getLocation(), title.getDescription(), title.getQuality(), title.getActors(), title.getPicture(), title.isLastAdded(), title.isMustWatch(), title.isFavorite(), title.isIncorrect(), title.getImdbRating() );
    }

}
