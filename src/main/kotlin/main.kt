import com.david.mocassin.view.MainView
import com.david.mocassin.view.styles.MainStyle
import javafx.scene.image.Image
import tornadofx.*

class MocassinApp: App(MainView::class, MainStyle::class) {
    init {
        addStageIcon(Image(resources[""]))
        reloadStylesheetsOnFocus()
        importStylesheet(resources["css/bootstrapfx.css"])
    }
}

fun main(args: Array<String>) {
    launch<MocassinApp>(args)
}