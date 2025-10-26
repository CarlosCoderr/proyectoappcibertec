package com.app.balance.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "balance_db"
<<<<<<< HEAD
        private const val DATABASE_VERSION = 7

        // Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_CATEGORIAS_SISTEMA = "categorias_sistema"
        const val TABLE_TRANSACCIONES = "transacciones"
        const val TABLE_TIPOS_CATEGORIA = "tipos_categoria"
        const val TABLE_COUNTRY_CODES = "country_codes"
        const val TABLE_DIVISAS = "divisas"
=======
        private const val DATABASE_VERSION = 1

        // Tablas
        const val TABLE_USUARIOS = "usuarios"
        const val TABLE_CATEGORIAS = "categorias"
        const val TABLE_TRANSACCIONES = "transacciones"
        const val TABLE_TIPOS_CATEGORIA = "tipos_categoria"
        const val TABLE_COUNTRY_CODES = "country_codes"
>>>>>>> origin/main

        // Columnas Usuario
        const val COL_USUARIO_ID = "id"
        const val COL_USUARIO_NOMBRE = "nombre"
        const val COL_USUARIO_APELLIDO = "apellido"
        const val COL_USUARIO_FECHA_NAC = "fecha_nacimiento"
        const val COL_USUARIO_GENERO = "genero"
        const val COL_USUARIO_CELULAR = "celular"
        const val COL_USUARIO_EMAIL = "email"
        const val COL_USUARIO_CONTRASENA = "contrasena"
        const val COL_USUARIO_DIVISA_ID = "divisa_id"
        const val COL_USUARIO_MONTO_TOTAL = "monto_total"

<<<<<<< HEAD
        const val COL_USUARIO_FOTO_PERFIL = "foto_perfil"

        // Columnas Categoría Sistema
        const val COL_CAT_SISTEMA_ID = "id"
        const val COL_CAT_SISTEMA_NOMBRE = "nombre"
        const val COL_CAT_SISTEMA_ICONO = "icono"
        const val COL_CAT_SISTEMA_USUARIO_ID = "usuario_id"
        const val COL_CAT_SISTEMA_TIPO_ID = "tipo_categoria_id"
        const val COL_CAT_SISTEMA_RUTA_IMAGEN = "ruta_imagen"

        const val COL_CAT_SISTEMA_COLOR = "color"

        // Columnas Transacción
        const val COL_TRANSACCION_ID = "id"
        const val COL_TRANSACCION_CATEGORIA_NOMBRE = "categoria_nombre"
        const val COL_TRANSACCION_CATEGORIA_ICONO = "categoria_icono"
        const val COL_TRANSACCION_CATEGORIA_RUTA_IMAGEN = "categoria_ruta_imagen"
        const val COL_TRANSACCION_TIPO_CATEGORIA_ID = "tipo_categoria_id"
        const val COL_TRANSACCION_TIPO_CATEGORIA_NOMBRE = "tipo_categoria_nombre"
=======
        // Columnas Categoría
        const val COL_CATEGORIA_ID = "id"
        const val COL_CATEGORIA_NOMBRE = "nombre"
        const val COL_CATEGORIA_ICONO = "icono"
        const val COL_CATEGORIA_USUARIO_ID = "usuario_id"
        const val COL_CATEGORIA_TIPO_ID = "tipo_categoria_id"
        const val COL_CATEGORIA_RUTA_IMAGEN = "ruta_imagen"

        // Columnas Transacción
        const val COL_TRANSACCION_ID = "id"
        const val COL_TRANSACCION_CATEGORIA_ID = "categoria_id"
>>>>>>> origin/main
        const val COL_TRANSACCION_MONTO = "monto"
        const val COL_TRANSACCION_FECHA = "fecha"
        const val COL_TRANSACCION_COMENTARIO = "comentario"
        const val COL_TRANSACCION_USUARIO_ID = "usuario_id"
<<<<<<< HEAD
        const val COL_TRANSACCION_CATEGORIA_COLOR = "categoria_color"
=======
>>>>>>> origin/main

        // Columnas Tipo Categoría
        const val COL_TIPO_ID = "id"
        const val COL_TIPO_NOMBRE = "nombre"

<<<<<<< HEAD

=======
>>>>>>> origin/main
        // Columnas Country Code
        const val COL_COUNTRY_ID = "id"
        const val COL_COUNTRY_NOMBRE = "nombre"
        const val COL_COUNTRY_CODIGO = "codigo"
        const val COL_COUNTRY_BANDERA_URL = "bandera_url"
<<<<<<< HEAD

        // Columnas Divisas
        const val COL_DIVISA_ID = "id"
        const val COL_DIVISA_NOMBRE = "nombre"
        const val COL_DIVISA_CODIGO = "codigo"
        const val COL_DIVISA_BANDERA_URL = "bandera_url"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de tipos de categoría
=======
    }

    override fun onCreate(db: SQLiteDatabase) {
        //  tabla de tipos de categoría
>>>>>>> origin/main
        val createTipoCategoriaTable = """
            CREATE TABLE $TABLE_TIPOS_CATEGORIA (
                $COL_TIPO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TIPO_NOMBRE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTipoCategoriaTable)

<<<<<<< HEAD
        // Insertar tipos de categoría
=======
        // Insertar tipos de categoría predefinidos
>>>>>>> origin/main
        db.execSQL("INSERT INTO $TABLE_TIPOS_CATEGORIA ($COL_TIPO_NOMBRE) VALUES ('Necesidad')")
        db.execSQL("INSERT INTO $TABLE_TIPOS_CATEGORIA ($COL_TIPO_NOMBRE) VALUES ('Deseo')")
        db.execSQL("INSERT INTO $TABLE_TIPOS_CATEGORIA ($COL_TIPO_NOMBRE) VALUES ('Ahorro')")

<<<<<<< HEAD
        // Tabla de country codes
=======
        // tabla de country codes
>>>>>>> origin/main
        val createCountryCodeTable = """
            CREATE TABLE $TABLE_COUNTRY_CODES (
                $COL_COUNTRY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_COUNTRY_NOMBRE TEXT NOT NULL,
<<<<<<< HEAD
                $COL_COUNTRY_CODIGO TEXT NOT NULL UNIQUE,
                $COL_COUNTRY_BANDERA_URL TEXT
=======
                $COL_COUNTRY_CODIGO TEXT NOT NULL UNIQUE
>>>>>>> origin/main
            )
        """.trimIndent()
        db.execSQL(createCountryCodeTable)

<<<<<<< HEAD
        // NUEVA TABLA: Divisas
        val createDivisasTable = """
            CREATE TABLE $TABLE_DIVISAS (
                $COL_DIVISA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DIVISA_NOMBRE TEXT NOT NULL,
                $COL_DIVISA_CODIGO TEXT NOT NULL UNIQUE,
                $COL_DIVISA_BANDERA_URL TEXT
            )
        """.trimIndent()
        db.execSQL(createDivisasTable)

        // Tabla de usuarios
=======
        //  tabla de usuarios
>>>>>>> origin/main
        val createUsuarioTable = """
            CREATE TABLE $TABLE_USUARIOS (
                $COL_USUARIO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USUARIO_NOMBRE TEXT NOT NULL,
                $COL_USUARIO_APELLIDO TEXT NOT NULL,
                $COL_USUARIO_FECHA_NAC TEXT NOT NULL,
                $COL_USUARIO_GENERO TEXT NOT NULL,
                $COL_USUARIO_CELULAR TEXT NOT NULL,
                $COL_USUARIO_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USUARIO_CONTRASENA TEXT NOT NULL,
                $COL_USUARIO_DIVISA_ID INTEGER NOT NULL,
                $COL_USUARIO_MONTO_TOTAL REAL DEFAULT 0,
<<<<<<< HEAD
                $COL_USUARIO_FOTO_PERFIL TEXT,
                FOREIGN KEY ($COL_USUARIO_DIVISA_ID) REFERENCES $TABLE_DIVISAS ($COL_DIVISA_ID)
=======
                FOREIGN KEY ($COL_USUARIO_DIVISA_ID) REFERENCES $TABLE_COUNTRY_CODES ($COL_COUNTRY_ID)
>>>>>>> origin/main
            )
        """.trimIndent()
        db.execSQL(createUsuarioTable)

<<<<<<< HEAD
        // Tabla de Categorías del Sistema
        val createCategoriaSistemaTable = """
            CREATE TABLE $TABLE_CATEGORIAS_SISTEMA (
                $COL_CAT_SISTEMA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CAT_SISTEMA_NOMBRE TEXT NOT NULL,
                $COL_CAT_SISTEMA_ICONO TEXT DEFAULT 'default',
                $COL_CAT_SISTEMA_USUARIO_ID INTEGER NOT NULL,
                $COL_CAT_SISTEMA_TIPO_ID INTEGER NOT NULL,
                $COL_CAT_SISTEMA_RUTA_IMAGEN TEXT,
                $COL_CAT_SISTEMA_COLOR INTEGER,
                FOREIGN KEY ($COL_CAT_SISTEMA_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COL_USUARIO_ID),
                FOREIGN KEY ($COL_CAT_SISTEMA_TIPO_ID) REFERENCES $TABLE_TIPOS_CATEGORIA ($COL_TIPO_ID)
            )
        """.trimIndent()
        db.execSQL(createCategoriaSistemaTable)

        // Tabla de Transacciones
        val createTransaccionTable = """
            CREATE TABLE $TABLE_TRANSACCIONES (
                $COL_TRANSACCION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TRANSACCION_CATEGORIA_NOMBRE TEXT NOT NULL,
                $COL_TRANSACCION_CATEGORIA_ICONO TEXT NOT NULL,
                $COL_TRANSACCION_CATEGORIA_RUTA_IMAGEN TEXT,
                $COL_TRANSACCION_TIPO_CATEGORIA_ID INTEGER NOT NULL,
                $COL_TRANSACCION_CATEGORIA_COLOR INTEGER,
                $COL_TRANSACCION_TIPO_CATEGORIA_NOMBRE TEXT NOT NULL,
=======
        //  tabla de categorías
        val createCategoriaTable = """
            CREATE TABLE $TABLE_CATEGORIAS (
                $COL_CATEGORIA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CATEGORIA_NOMBRE TEXT NOT NULL,
                $COL_CATEGORIA_ICONO TEXT DEFAULT 'default',
                $COL_CATEGORIA_USUARIO_ID INTEGER NOT NULL,
                $COL_CATEGORIA_TIPO_ID INTEGER NOT NULL,
                $COL_CATEGORIA_RUTA_IMAGEN TEXT,
                FOREIGN KEY ($COL_CATEGORIA_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COL_USUARIO_ID),
                FOREIGN KEY ($COL_CATEGORIA_TIPO_ID) REFERENCES $TABLE_TIPOS_CATEGORIA ($COL_TIPO_ID)
            )
        """.trimIndent()
        db.execSQL(createCategoriaTable)

        //tabla de transacciones
        val createTransaccionTable = """
            CREATE TABLE $TABLE_TRANSACCIONES (
                $COL_TRANSACCION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TRANSACCION_CATEGORIA_ID INTEGER NOT NULL,
>>>>>>> origin/main
                $COL_TRANSACCION_MONTO REAL NOT NULL,
                $COL_TRANSACCION_FECHA TEXT NOT NULL,
                $COL_TRANSACCION_COMENTARIO TEXT,
                $COL_TRANSACCION_USUARIO_ID INTEGER NOT NULL,
<<<<<<< HEAD
=======
                FOREIGN KEY ($COL_TRANSACCION_CATEGORIA_ID) REFERENCES $TABLE_CATEGORIAS ($COL_CATEGORIA_ID),
>>>>>>> origin/main
                FOREIGN KEY ($COL_TRANSACCION_USUARIO_ID) REFERENCES $TABLE_USUARIOS ($COL_USUARIO_ID)
            )
        """.trimIndent()
        db.execSQL(createTransaccionTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
<<<<<<< HEAD
        // Eliminar tablas si existen
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACCIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS_SISTEMA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIVISAS")
=======
        // Eliminar tablas si existen (para desarrollo)
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACCIONES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIAS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
>>>>>>> origin/main
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TIPOS_CATEGORIA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_COUNTRY_CODES")
        onCreate(db)
    }
}