import com.david.mocassin.utils.lang.SupportedLocale
import com.david.mocassin.view.MainView
import com.david.mocassin.view.styles.MainStyle
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import java.io.File
import java.nio.file.Paths
import java.util.*

class MocassinApp: App(MainView::class, MainStyle::class) {
    //override val configBasePath = Paths.get("/etc/mocassin/conf")

    init {
        addStageIcon(Image(resources[""]))
        reloadStylesheetsOnFocus()
        importStylesheet(resources["css/bootstrapfx.css"])
        importStylesheet(resources["css/main.css"])

        loadConfig()
        SupportedLocale.isSupportedLocal(Locale.forLanguageTag(config["locale"].toString()))?.let { loc ->
            FX.locale = loc.local
        }

        FX.localeProperty().onChange {
            information("Default language to ${FX.locale.displayLanguage}", "restart the app to apply the new language")
            with(config) {
                set("locale" to it)
                save()
            }
        }
    }
}

fun main(args: Array<String>) {
    launch<MocassinApp>(args)
}