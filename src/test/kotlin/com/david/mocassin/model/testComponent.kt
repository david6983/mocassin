package com.david.mocassin.model

import com.david.mocassin.model.c_components.*
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler
import java.io.File
import java.io.IOException

fun main() {
    // c'est variable seront stocké dans le model
    // premiere enumeration de l'utilisateur en typedef
    val e1 = Cenum("EnumPos")
    e1.add("TOP")
    e1.add("BOTTOM")
    // calcData union
    val ccd = Cunion("calcData")
    ccd.add(Cvariable("perimeter", CtypeEnum.FLOAT))
    ccd.add(Cvariable("surface", CtypeEnum.FLOAT))
    // multipos structure
    val mltp = CuserStructure("MultiPos")
    mltp.add(Cvariable("x1", CtypeEnum.INT, isComparable = true))
    mltp.add(Cvariable("x2", CtypeEnum.INT, isComparable = true))
    mltp.add(Cvariable("y1", CtypeEnum.INT, isComparable = true))
    mltp.add(Cvariable("y2", CtypeEnum.INT, isComparable = true))
    // data union
    val dtu = Cunion("Data")
    dtu.add(Cvariable("posFromCenter", CtypeEnum.INT))
    dtu.add(Cvariable("calculusFromUnion", ccd))
    dtu.add(Cvariable("multiplePos", mltp))
    // Rectangle structure
    val rcts = CuserStructure("Rectangle")
    rcts.add(Cvariable("x", CtypeEnum.INT, isComparable = true))
    rcts.add(Cvariable("y", CtypeEnum.INT))
    rcts.add(Cvariable("width", CtypeEnum.FLOAT, isComparable = true))
    rcts.add(Cvariable("height", CtypeEnum.FLOAT))
    rcts.add(Cvariable("data", dtu, isComparable = true))
    val model = UserModel("GEOMETRY")
    model.add(e1)
    model.add(ccd)
    model.add(e1)
    model.add(dtu)
    model.add(mltp)
    model.add(rcts)
    val slist = SlistModel(model)
    slist.addVariable(CtypeEnum.INT)
    slist.addVariable(CtypeEnum.INT)
    slist.addVariable(CtypeEnum.FLOAT)
    slist.addVariable(CtypeEnum.STRING)
    /* ------------------------------------------------------------------------ */
    /* You should do this ONLY ONCE in the whole application life-cycle:        */

    /* Create and adjust the configuration singleton */
    val cfg = Configuration(Configuration.VERSION_2_3_29)
    try {
        cfg.setDirectoryForTemplateLoading(File("templates/"))
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // Recommended settings for new projects:
    cfg.defaultEncoding = "UTF-8"
    cfg.templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
    cfg.logTemplateExceptions = false
    cfg.wrapUncheckedExceptions = true
    cfg.fallbackOnNullLoopVariable = false
    /* ------------------------------------------------------------------------ */
    /* You usually do these for MULTIPLE TIMES in the application life-cycle:   */
    //TODO gerer la creation de dossier avec mkdir
    //TODO ameliorer le model car il est pas très propre
    model.generate(cfg, "out")
    slist.generate(cfg, "out")

    model.save("out/")
    slist.save("out/")
}