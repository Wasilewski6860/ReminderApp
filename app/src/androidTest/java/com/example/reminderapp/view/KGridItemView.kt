package com.example.reminderapp.view

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.reminderapp.R
import com.example.reminderapp.ui.GridItemCustomView
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.image.KImageView
import org.hamcrest.Matcher

object GridItemCustomViewResources {
    var IMAGE_VIEW = R.id.imageGridItem
    var COUNTER_TEXT_VIEW = R.id.textCounterGridItem
    var TITLE_TEXT_VIEW = R.id.nameTextGridItem
}


class KGridItemView(viewBuilder: ViewBuilder.() -> Unit) : KBaseView<KGridItemView>(viewBuilder) {
    val imageView = KImageView { withParent { withId(GridItemCustomViewResources.IMAGE_VIEW)  }  }
    val counterTextView = KTextView { withParent { withId(GridItemCustomViewResources.COUNTER_TEXT_VIEW) } }
    val titleTextView = KTextView { withParent { withId(GridItemCustomViewResources.TITLE_TEXT_VIEW) } }
}

