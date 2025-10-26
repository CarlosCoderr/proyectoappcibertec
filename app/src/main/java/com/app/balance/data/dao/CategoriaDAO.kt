package com.app.balance.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.model.Categoria

class CategoriaDAO(private val db: SQLiteDatabase, private val dbHelper: AppDatabaseHelper) {

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
    fun insertarCategoriaSistema(categoria: Categoria): Long {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_CAT_SISTEMA_NOMBRE, categoria.nombre)
            put(AppDatabaseHelper.COL_CAT_SISTEMA_ICONO, categoria.icono)
            put(AppDatabaseHelper.COL_CAT_SISTEMA_USUARIO_ID, categoria.usuarioId)
            put(AppDatabaseHelper.COL_CAT_SISTEMA_TIPO_ID, categoria.tipoCategoriaId)
            put(AppDatabaseHelper.COL_CAT_SISTEMA_RUTA_IMAGEN, categoria.rutaImagen)
            put(AppDatabaseHelper.COL_CAT_SISTEMA_COLOR, categoria.color)
        }
        return db.insert(AppDatabaseHelper.TABLE_CATEGORIAS_SISTEMA, null, valores)
    }

    fun obtenerCategoriasSistemaPorUsuario(usuarioId: Int): List<Categoria> {
        val categorias = mutableListOf<Categoria>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_CATEGORIAS_SISTEMA,
            null,
            "${AppDatabaseHelper.COL_CAT_SISTEMA_USUARIO_ID} = ?",
<<<<<<< HEAD
=======
    fun insertarCategoria(categoria: Categoria): Long {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_CATEGORIA_NOMBRE, categoria.nombre)
            put(AppDatabaseHelper.COL_CATEGORIA_ICONO, categoria.icono)
            put(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID, categoria.usuarioId)
            put(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID, categoria.tipoCategoriaId)
            put(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN, categoria.rutaImagen)
        }
        return db.insert(AppDatabaseHelper.TABLE_CATEGORIAS, null, valores)
    }

    fun obtenerCategoriasPorUsuario(usuarioId: Int): List<Categoria> {
        val categorias = mutableListOf<Categoria>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_CATEGORIAS,
            null,
            "${AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID} = ?",
>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
            arrayOf(usuarioId.toString()),
            null,
            null,
            null
        )
        while (cursor.moveToNext()) {
            categorias.add(
                Categoria(
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_NOMBRE)),
                    icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_ICONO)),
                    usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_USUARIO_ID)),
                    tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_TIPO_ID)),
                    rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_RUTA_IMAGEN)),
                    color = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CAT_SISTEMA_COLOR))
<<<<<<< HEAD
=======
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ID)),
                    nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_NOMBRE)),
                    icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ICONO)),
                    usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID)),
                    tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID)),
                    rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN))
>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
                )
            )
        }
        cursor.close()
        return categorias
    }

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
    fun eliminarCategoriaSistema(categoriaId: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_CATEGORIAS_SISTEMA,
            "${AppDatabaseHelper.COL_CAT_SISTEMA_ID} = ?",
<<<<<<< HEAD
=======
    fun obtenerCategoriaPorId(categoriaId: Int): Categoria? {
        val cursor = db.query(
            AppDatabaseHelper.TABLE_CATEGORIAS,
            null,
            "${AppDatabaseHelper.COL_CATEGORIA_ID} = ?",
            arrayOf(categoriaId.toString()),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val categoria = Categoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_NOMBRE)),
                icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ICONO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID)),
                tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID)),
                rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN))
            )
            cursor.close()
            categoria
        } else {
            cursor.close()
            null
        }
    }

    fun actualizarCategoria(categoria: Categoria): Int {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_CATEGORIA_NOMBRE, categoria.nombre)
            put(AppDatabaseHelper.COL_CATEGORIA_ICONO, categoria.icono)
            put(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID, categoria.tipoCategoriaId)
            put(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN, categoria.rutaImagen)
        }
        return db.update(
            AppDatabaseHelper.TABLE_CATEGORIAS,
            valores,
            "${AppDatabaseHelper.COL_CATEGORIA_ID} = ?",
            arrayOf(categoria.id.toString())
        )
    }

    fun eliminarCategoria(categoriaId: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_CATEGORIAS,
            "${AppDatabaseHelper.COL_CATEGORIA_ID} = ?",
>>>>>>> origin/main
=======
>>>>>>> 589dd31 (Desarollo de divisas, balance, Desarollo de header con menu, Dashboard fragment, gastos fragment, balance fragment, ademas de creacion de categoria Activity y transaccion gasto, persistencia de datos y CRUD completo,)
            arrayOf(categoriaId.toString())
        )
    }
}