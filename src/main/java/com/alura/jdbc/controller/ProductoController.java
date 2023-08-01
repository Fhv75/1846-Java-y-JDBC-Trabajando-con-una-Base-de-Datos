package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.model.Producto;

public class ProductoController {

    private ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO(new ConnectionFactory().getConnection());
    }

    public void modificar(String nombre, String descripcion, int cantidad, Integer id) throws SQLException {
        final Connection conn = new ConnectionFactory().getConnection();
        try (conn) {
            // Devuelve true cuando el resultado es un java.sql.ResultSet (SELECT, SHOW,
            // DESCRIBE or EXPLAIN) y
            // false cuando el resultado no devuelve contenido (DELETE, UPDATE, COUNT).
            final PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE PRODUCTO SET NOMBRE = ?, DESCRIPCION = ?, CANTIDAD = ? WHERE ID = ?",
                    Statement.RETURN_GENERATED_KEYS);
            try (stmt) {

                stmt.setString(1, nombre);
                stmt.setString(2, descripcion);
                stmt.setInt(3, cantidad);
                stmt.setInt(4, id);

                stmt.execute();

                ResultSet resultSet = stmt.getGeneratedKeys();
                while (resultSet.next()) {
                    System.out.printf("Actualizado producto con ID %d", resultSet.getInt(1));
                }
            }
        }
    }

    public int eliminar(Integer id) throws SQLException {
        final Connection conn = new ConnectionFactory().getConnection();

        try (conn) {
            final PreparedStatement stmt = conn.prepareStatement("DELETE FROM PRODUCTOS WHERE ID = ?",
                    Statement.RETURN_GENERATED_KEYS);
            try (stmt) {
                stmt.setInt(1, id);
                stmt.execute();

                ResultSet resultSet = stmt.getGeneratedKeys();
                while (resultSet.next()) {
                    System.out.printf("Eliminado producto con ID %d", resultSet.getInt(1));
                }

                int updateCount = stmt.getUpdateCount();

                return updateCount;
            }
        }
    }

    public List<Producto> listarProductos() throws SQLException {
        return productoDAO.listarProductos();
    }

    public void guardar(Producto producto) {
        productoDAO.guardarProducto(producto);
    }

}
