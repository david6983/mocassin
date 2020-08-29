import com.david.mocassin.utils.lang.SupportedLocale
import com.david.mocassin.view.MainView
import com.david.mocassin.view.components.fragments.SplashScreen
import com.david.mocassin.view.styles.MainStyle
import javafx.beans.property.SimpleBooleanProperty
import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*
import java.util.*

class MocassinApp: App(MainView::class, MainStyle::class) {
    //override val configBasePath = Paths.get("/etc/mocassin/conf")
    val isSplashScreenDisplayed = SimpleBooleanProperty(true)

    init {
        addStageIcon(Image(resources["icons/mocassin.png"]))
        reloadStylesheetsOnFocus()
        importStylesheet(resources["css/bootstrapfx.css"])
        importStylesheet(resources["css/main.css"])

        loadConfig()
        SupportedLocale.isSupportedLocal(Locale.forLanguageTag(config["locale"].toString()))?.let { loc ->
            FX.locale = loc.local
        }

        config["splashscreen"]?.let {
            isSplashScreenDisplayed.value = it as Boolean?
        }

        FX.localeProperty().onChange {
            with(config) {
                set("locale" to it)
                save()
            }
        }

        isSplashScreenDisplayed.onChange {
            with(config) {
                set("splashscreen" to it)
                save()
            }
        }
    }

    override fun start(stage: Stage) {
        super.start(stage)
        if (isSplashScreenDisplayed.value) {
            find<SplashScreen>().openModal(stageStyle = StageStyle.TRANSPARENT)
        }
    }

    override fun shouldShowPrimaryStage() = !isSplashScreenDisplayed.value
}

fun main(args: Array<String>) {
    launch<MocassinApp>(args)
}