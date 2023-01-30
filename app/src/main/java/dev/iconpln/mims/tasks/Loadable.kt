package dev.iconpln.mims.tasks

interface Loadable {
    fun setLoading(show: Boolean, title: String, message: String)
    fun setFinish(result: Boolean, message: String)

}
