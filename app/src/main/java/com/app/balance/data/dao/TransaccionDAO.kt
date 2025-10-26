package com.app.balance.data.dao

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.app.balance.data.AppDatabaseHelper
import com.app.balance.model.Categoria
import com.app.balance.model.TipoCategoria
import com.app.balance.model.Transaccion
import com.app.balance.model.TransaccionConDetalles

class TransaccionDAO(private val db: SQLiteDatabase, private val dbHelper: AppDatabaseHelper) {

<<<<<<< HEAD
    fun insertarTransaccion(
        categoriaNombre: String,
        categoriaIcono: String,
        categoriaRutaImagen: String?,
        categoriaColor: Int?,
        tipoCategoriaId: Int,
        tipoCategoriaNombre: String,
        monto: Double,
        fecha: String,
        comentario: String?,
        usuarioId: Int
    ): Long {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_NOMBRE, categoriaNombre)
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ICONO, categoriaIcono)
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_RUTA_IMAGEN, categoriaRutaImagen)
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_COLOR, categoriaColor)
            put(AppDatabaseHelper.COL_TRANSACCION_TIPO_CATEGORIA_ID, tipoCategoriaId)
            put(AppDatabaseHelper.COL_TRANSACCION_TIPO_CATEGORIA_NOMBRE, tipoCategoriaNombre)
            put(AppDatabaseHelper.COL_TRANSACCION_MONTO, monto)
            put(AppDatabaseHelper.COL_TRANSACCION_FECHA, fecha)
            put(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO, comentario)
            put(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID, usuarioId)
=======
    fun insertarTransaccion(transaccion: Transaccion): Long {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID, transaccion.categoriaId)
            put(AppDatabaseHelper.COL_TRANSACCION_MONTO, transaccion.monto)
            put(AppDatabaseHelper.COL_TRANSACCION_FECHA, transaccion.fecha)
            put(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO, transaccion.comentario)
            put(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID, transaccion.usuarioId)
>>>>>>> origin/main
        }
        return db.insert(AppDatabaseHelper.TABLE_TRANSACCIONES, null, valores)
    }

    fun obtenerTransaccionesPorUsuario(usuarioId: Int): List<TransaccionConDetalles> {
        val transacciones = mutableListOf<TransaccionConDetalles>()
        val query = """
<<<<<<< HEAD
        SELECT * FROM ${AppDatabaseHelper.TABLE_TRANSACCIONES}
        WHERE ${AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID} = ?
        ORDER BY ${AppDatabaseHelper.COL_TRANSACCION_FECHA} DESC
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))

        while (cursor.moveToNext()) {
            val transaccion = Transaccion(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_ID)),
                categoriaId = 0,
=======
            SELECT t.*, c.*, tc.* 
            FROM ${AppDatabaseHelper.TABLE_TRANSACCIONES} t
            JOIN ${AppDatabaseHelper.TABLE_CATEGORIAS} c ON t.${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = c.${AppDatabaseHelper.COL_CATEGORIA_ID}
            JOIN ${AppDatabaseHelper.TABLE_TIPOS_CATEGORIA} tc ON c.${AppDatabaseHelper.COL_CATEGORIA_TIPO_ID} = tc.${AppDatabaseHelper.COL_TIPO_ID}
            WHERE t.${AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID} = ?
            ORDER BY t.${AppDatabaseHelper.COL_TRANSACCION_FECHA} DESC
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString()))
        while (cursor.moveToNext()) {
            val transaccion = Transaccion(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_ID)),
                categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID)),
>>>>>>> origin/main
                monto = cursor.getDouble(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_MONTO)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_FECHA)),
                comentario = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID))
            )
<<<<<<< HEAD

            //  Leer el color
            val colorIndex = cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_COLOR)
            val color = if (cursor.isNull(colorIndex)) null else cursor.getInt(colorIndex)

            val categoria = Categoria(
                id = 0,
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_NOMBRE)),
                icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ICONO)),
                usuarioId = usuarioId,
                tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_TIPO_CATEGORIA_ID)),
                rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_RUTA_IMAGEN)),
                color = color
            )

            val tipoCategoria = TipoCategoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_TIPO_CATEGORIA_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_TIPO_CATEGORIA_NOMBRE))
            )

=======
            val categoria = Categoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_NOMBRE)),
                icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ICONO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID)),
                tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID)),
                rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN))
            )
            val tipoCategoria = TipoCategoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_NOMBRE))
            )
>>>>>>> origin/main
            transacciones.add(TransaccionConDetalles(transaccion, categoria, tipoCategoria))
        }
        cursor.close()
        return transacciones
    }

<<<<<<< HEAD
=======
    fun obtenerTransaccionesPorFecha(usuarioId: Int, fecha: String): List<TransaccionConDetalles> {
        val transacciones = mutableListOf<TransaccionConDetalles>()
        val query = """
            SELECT t.*, c.*, tc.* 
            FROM ${AppDatabaseHelper.TABLE_TRANSACCIONES} t
            JOIN ${AppDatabaseHelper.TABLE_CATEGORIAS} c ON t.${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = c.${AppDatabaseHelper.COL_CATEGORIA_ID}
            JOIN ${AppDatabaseHelper.TABLE_TIPOS_CATEGORIA} tc ON c.${AppDatabaseHelper.COL_CATEGORIA_TIPO_ID} = tc.${AppDatabaseHelper.COL_TIPO_ID}
            WHERE t.${AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID} = ? AND t.${AppDatabaseHelper.COL_TRANSACCION_FECHA} = ?
            ORDER BY t.${AppDatabaseHelper.COL_TRANSACCION_FECHA} DESC
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString(), fecha))
        while (cursor.moveToNext()) {
            val transaccion = Transaccion(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_ID)),
                categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID)),
                monto = cursor.getDouble(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_MONTO)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_FECHA)),
                comentario = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID))
            )
            val categoria = Categoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_NOMBRE)),
                icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ICONO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID)),
                tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID)),
                rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN))
            )
            val tipoCategoria = TipoCategoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_NOMBRE))
            )
            transacciones.add(TransaccionConDetalles(transaccion, categoria, tipoCategoria))
        }
        cursor.close()
        return transacciones
    }

    fun obtenerTransaccionesPorCategoria(categoriaId: Int): List<Transaccion> {
        val transacciones = mutableListOf<Transaccion>()
        val cursor = db.query(
            AppDatabaseHelper.TABLE_TRANSACCIONES,
            null,
            "${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = ?",
            arrayOf(categoriaId.toString()),
            null,
            null,
            "${AppDatabaseHelper.COL_TRANSACCION_FECHA} DESC"
        )
        while (cursor.moveToNext()) {
            transacciones.add(
                Transaccion(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_ID)),
                    categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID)),
                    monto = cursor.getDouble(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_MONTO)),
                    fecha = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_FECHA)),
                    comentario = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO)),
                    usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID))
                )
            )
        }
        cursor.close()
        return transacciones
    }

    fun obtenerSumaTransaccionesPorTipo(usuarioId: Int, tipoId: Int): Double {
        val query = """
            SELECT COALESCE(SUM(t.${AppDatabaseHelper.COL_TRANSACCION_MONTO}), 0) as total
            FROM ${AppDatabaseHelper.TABLE_TRANSACCIONES} t
            JOIN ${AppDatabaseHelper.TABLE_CATEGORIAS} c ON t.${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = c.${AppDatabaseHelper.COL_CATEGORIA_ID}
            WHERE t.${AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID} = ? AND c.${AppDatabaseHelper.COL_CATEGORIA_TIPO_ID} = ?
        """.trimIndent()
        val cursor = db.rawQuery(query, arrayOf(usuarioId.toString(), tipoId.toString()))
        var total = 0.0
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"))
        }
        cursor.close()
        return total
    }

    fun actualizarTransaccion(transaccion: Transaccion): Int {
        val valores = ContentValues().apply {
            put(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID, transaccion.categoriaId)
            put(AppDatabaseHelper.COL_TRANSACCION_MONTO, transaccion.monto)
            put(AppDatabaseHelper.COL_TRANSACCION_FECHA, transaccion.fecha)
            put(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO, transaccion.comentario)
        }
        return db.update(
            AppDatabaseHelper.TABLE_TRANSACCIONES,
            valores,
            "${AppDatabaseHelper.COL_TRANSACCION_ID} = ?",
            arrayOf(transaccion.id.toString())
        )
    }

>>>>>>> origin/main
    fun eliminarTransaccion(transaccionId: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_TRANSACCIONES,
            "${AppDatabaseHelper.COL_TRANSACCION_ID} = ?",
            arrayOf(transaccionId.toString())
        )
    }
<<<<<<< HEAD
}
=======

    fun eliminarTransaccionesPorCategoria(categoriaId: Int): Int {
        return db.delete(
            AppDatabaseHelper.TABLE_TRANSACCIONES,
            "${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = ?",
            arrayOf(categoriaId.toString())
        )
    }

    /**
     * NUEVO: obtener una transacción por ID, incluyendo la categoría y el tipo.
     * Útil para "Editar" desde el diálogo de detalles.
     */
    fun obtenerTransaccionPorId(id: Int): TransaccionConDetalles? {
        val query = """
            SELECT t.*, c.*, tc.* 
            FROM ${AppDatabaseHelper.TABLE_TRANSACCIONES} t
            JOIN ${AppDatabaseHelper.TABLE_CATEGORIAS} c 
                ON t.${AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID} = c.${AppDatabaseHelper.COL_CATEGORIA_ID}
            JOIN ${AppDatabaseHelper.TABLE_TIPOS_CATEGORIA} tc 
                ON c.${AppDatabaseHelper.COL_CATEGORIA_TIPO_ID} = tc.${AppDatabaseHelper.COL_TIPO_ID}
            WHERE t.${AppDatabaseHelper.COL_TRANSACCION_ID} = ?
            LIMIT 1
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        var result: TransaccionConDetalles? = null
        if (cursor.moveToFirst()) {
            val transaccion = Transaccion(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_ID)),
                categoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_CATEGORIA_ID)),
                monto = cursor.getDouble(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_MONTO)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_FECHA)),
                comentario = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_COMENTARIO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TRANSACCION_USUARIO_ID))
            )
            val categoria = Categoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_NOMBRE)),
                icono = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_ICONO)),
                usuarioId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_USUARIO_ID)),
                tipoCategoriaId = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_TIPO_ID)),
                rutaImagen = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_CATEGORIA_RUTA_IMAGEN))
            )
            val tipoCategoria = TipoCategoria(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(AppDatabaseHelper.COL_TIPO_NOMBRE))
            )
            result = TransaccionConDetalles(transaccion, categoria, tipoCategoria)
        }
        cursor.close()
        return result
    }
}
>>>>>>> origin/main
