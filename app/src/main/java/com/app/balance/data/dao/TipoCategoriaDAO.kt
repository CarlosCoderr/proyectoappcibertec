package com.app.balance.data.dao

import android.database.sqlite.SQLiteDatabase
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.model.TipoCategoria

class TipoCategoriaDAO(private val db: SQLiteDatabase, private val dbHelper: AppDatabaseHelper) {

<<<<<<< HEAD
<<<<<<< HEAD
=======
    fun obtenerTodosTipos(): List<TipoCategoria> {
        val tipos = mutableListOf<TipoCategoria>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_TIPOS_CATEGORIA,
            null,
            null,
            null,
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            tipos.add(
                TipoCategoria(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_NOMBRE))
                )
            )
        }
        cursor.close()
        return tipos
    }

>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
    fun obtenerTipoPorId(tipoId: Int): TipoCategoria? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_TIPOS_CATEGORIA,
            null,
            "${AppDatabaseHelper.COL_TIPO_ID} = ?",
            arrayOf(tipoId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val tipo = TipoCategoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_NOMBRE))
            )
            cursor.close()
            tipo
        } else {
            cursor.close()
            null
        }
    }
}