package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.alura.model.Producto;

public class ProductoDAO {
    final private Connection conn;

    public ProductoDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Producto> listarProductos() throws SQLException {

        try (this.conn) {
            final PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM productos");

            try (stmt) {
                stmt.execute();

                final ResultSet resultSet = stmt.getResultSet();
                List<Producto> productos = new ArrayList<>();

                while (resultSet.next()) {
                    Producto producto = new Producto(resultSet.getString("nombre"), resultSet.getString("descripcion"),
                            resultSet.getInt("cantidad"));
                    producto.setId(resultSet.getInt("id"));
                    productos.add(producto);
                }
                return productos;
            }
        }
    }

    public void guardarProducto(Producto producto) {
        int cantidad = producto.getCantidad();

        try (this.conn) {
            final PreparedStatement stmt = this.conn.prepareStatement(
                    "INSERT INTO productos (nombre, descripcion, cantidad) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            try (stmt) {
                ejecutarRegistro(stmt, producto, cantidad);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Cierro el statement y la conexion (no es necesario si pongo a conn y a stmt
        // dentro de un try(){})
        // stmt.close();
        // conn.close();
    }

    private void ejecutarRegistro(PreparedStatement stmt, Producto p, int cantidad)
            throws SQLException {

        stmt.setString(1, p.getNombre());
        stmt.setString(2, p.getDescripcion());
        stmt.setInt(3, cantidad);

        stmt.execute();

        final ResultSet resultSet = stmt.getGeneratedKeys();
        // Cerrar resultSet ya no es necesario gracias a try w resources (ResultSet
        // implementa autoclosable)
        try (resultSet) {
            while (resultSet.next()) {
                System.out.printf("Insertado producto con ID %d\n", resultSet.getInt(1));
            }
        }

        // resultSet.close();
    }

}
