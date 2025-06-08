package com.hotel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SistemaReservaHotel extends JFrame {

    private Connection conn;

    private JTextField tfEmailLogin;
    private JPasswordField pfSenhaLogin;

    private JComboBox<String> cbQuartos;
    private JTextField tfCheckIn, tfCheckOut;
    private JPanel painelServicos;
    private List<JCheckBox> checkBoxesServicos;
    private List<ServicoExtra> servicos;

    private int idClienteLogado = -1;

    public SistemaReservaHotel() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sistema_hotel", "root", "Jrl-122133");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco: " + ex.getMessage());
            System.exit(1);
        }

        setTitle("Sistema de Reserva de Hotel");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initServicos();
        initLoginUI();
    }

    private void initServicos() {
        servicos = Arrays.asList(
            new ServicoExtra("Lavanderia", "Serviço de lavagem e passagem de roupas", 50.00),
            new ServicoExtra("Translado Aeroporto", "Transporte de ida e volta ao aeroporto", 100.00),
            new ServicoExtra("Café da Manhã", "Café da manhã servido no quarto", 30.00),
            new ServicoExtra("Estacionamento", "Vaga coberta para o carro", 40.00),
            new ServicoExtra("Wi-Fi Premium", "Internet de alta velocidade no quarto", 20.00)
        );
    }

    private void initLoginUI() {
        getContentPane().removeAll();
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel lblEmail = new JLabel("Email:");
        JLabel lblSenha = new JLabel("Senha:");
        tfEmailLogin = new JTextField(20);
        pfSenhaLogin = new JPasswordField(20);
        JButton btnLogin = new JButton("Login");
        JButton btnCadastro = new JButton("Cadastrar Novo Cliente");

        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; panel.add(lblEmail, c);
        c.gridx = 1; panel.add(tfEmailLogin, c);
        c.gridx = 0; c.gridy = 1; panel.add(lblSenha, c);
        c.gridx = 1; panel.add(pfSenhaLogin, c);
        c.gridy = 2; c.gridx = 0; c.gridwidth = 2;
        panel.add(btnLogin, c);
        c.gridy = 3;
        panel.add(btnCadastro, c);

        btnLogin.addActionListener(e -> loginCliente());
        btnCadastro.addActionListener(e -> cadastroCliente());

        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private void loginCliente() {
        String email = tfEmailLogin.getText().trim();
        String senha = new String(pfSenhaLogin.getPassword());

        if(email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha email e senha.");
            return;
        }

        try {
            String sql = "SELECT id FROM clientes WHERE email = ? AND senha = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                idClienteLogado = rs.getInt("id");
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                mostrarTelaReserva();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha incorretos.");
            }
            rs.close();
            ps.close();
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar cliente: " + ex.getMessage());
        }
    }

    private void cadastroCliente() {
        String email = JOptionPane.showInputDialog(this, "Digite seu email:");
        if(email == null || email.trim().isEmpty()) return;
        String nome = JOptionPane.showInputDialog(this, "Digite seu nome completo:");
        if(nome == null || nome.trim().isEmpty()) return;
        String cpf = JOptionPane.showInputDialog(this, "Digite seu CPF:");
        if(cpf == null || cpf.trim().isEmpty()) return;
        String telefone = JOptionPane.showInputDialog(this, "Digite seu telefone:");
        if(telefone == null || telefone.trim().isEmpty()) return;
        String senha = JOptionPane.showInputDialog(this, "Digite sua senha:");
        if(senha == null || senha.trim().isEmpty()) return;

        try {
            String sqlCheck = "SELECT id FROM clientes WHERE email = ?";
            PreparedStatement psCheck = conn.prepareStatement(sqlCheck);
            psCheck.setString(1, email);
            ResultSet rs = psCheck.executeQuery();
            if(rs.next()) {
                JOptionPane.showMessageDialog(this, "Email já cadastrado.");
                rs.close();
                psCheck.close();
                return;
            }
            rs.close();
            psCheck.close();

            String sql = "INSERT INTO clientes (nome, cpf, telefone, email, senha) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, cpf);
            ps.setString(3, telefone);
            ps.setString(4, email);
            ps.setString(5, senha);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Agora faça login.");
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage());
        }
    }

    private void mostrarTelaReserva() {
        getContentPane().removeAll();
        setTitle("Fazer Reserva - Cliente ID: " + idClienteLogado);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Data Check-In (yyyy-MM-dd):"), c);
        c.gridx = 1;
        tfCheckIn = new JTextField(10);
        panel.add(tfCheckIn, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Data Check-Out (yyyy-MM-dd):"), c);
        c.gridx = 1;
        tfCheckOut = new JTextField(10);
        panel.add(tfCheckOut, c);

        c.gridx = 0; c.gridy = 2;
        panel.add(new JLabel("Quarto disponível:"), c);
        c.gridx = 1;
        cbQuartos = new JComboBox<>();
        panel.add(cbQuartos, c);

        JButton btnBuscarQuartos = new JButton("Buscar Quartos Disponíveis");
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        panel.add(btnBuscarQuartos, c);
        btnBuscarQuartos.addActionListener(e -> carregarQuartosDisponiveis());

        c.gridy = 4;
        painelServicos = new JPanel();
        painelServicos.setBorder(BorderFactory.createTitledBorder("Selecione Serviços Adicionais"));
        painelServicos.setLayout(new BoxLayout(painelServicos, BoxLayout.Y_AXIS));
        checkBoxesServicos = new ArrayList<>();
        for(ServicoExtra s : servicos) {
            JCheckBox cb = new JCheckBox(s.toString());
            checkBoxesServicos.add(cb);
            painelServicos.add(cb);
        }
        panel.add(painelServicos, c);

        JButton btnReservar = new JButton("Confirmar Reserva");
        c.gridy = 5;
        panel.add(btnReservar, c);
        btnReservar.addActionListener(e -> confirmarReserva());

        JButton btnCancelar = new JButton("Cancelar Reserva");
        c.gridy = 6;
        panel.add(btnCancelar, c);
        btnCancelar.addActionListener(e -> mostrarTelaCancelarReserva());

        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    private void carregarQuartosDisponiveis() {
        cbQuartos.removeAllItems();
        String checkInStr = tfCheckIn.getText().trim();
        String checkOutStr = tfCheckOut.getText().trim();

        if(checkInStr.isEmpty() || checkOutStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe as duas datas.");
            return;
        }

        try {
            LocalDate checkIn = LocalDate.parse(checkInStr);
            LocalDate checkOut = LocalDate.parse(checkOutStr);
            if(!checkOut.isAfter(checkIn)) {
                JOptionPane.showMessageDialog(this, "Check-out deve ser após check-in.");
                return;
            }

            String sql = "SELECT * FROM quartos WHERE id NOT IN (" +
                         "SELECT id_quarto FROM reservas WHERE (? < data_checkout AND ? > data_checkin)) AND situacao = 'Disponível'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(checkIn));
            ps.setDate(2, Date.valueOf(checkOut));
            ResultSet rs = ps.executeQuery();

            boolean encontrou = false;
            while(rs.next()) {
                encontrou = true;
                int id = rs.getInt("id");
                int numero = rs.getInt("numero");
                String tipo = rs.getString("tipo");
                double diaria = rs.getDouble("valor_diaria");
                cbQuartos.addItem(id + " - " + numero + " - " + tipo + " - R$" + diaria);
            }
            rs.close();
            ps.close();

            if(!encontrou) {
                JOptionPane.showMessageDialog(this, "Nenhum quarto disponível nesse período.");
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar quartos: " + ex.getMessage());
        }
    }

    private void confirmarReserva() {
        if(cbQuartos.getItemCount() == 0 || cbQuartos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um quarto disponível.");
            return;
        }

        try {
            String[] dados = ((String) cbQuartos.getSelectedItem()).split(" - ");
            int idQuarto = Integer.parseInt(dados[0]);
            LocalDate checkIn = LocalDate.parse(tfCheckIn.getText().trim());
            LocalDate checkOut = LocalDate.parse(tfCheckOut.getText().trim());
            long dias = ChronoUnit.DAYS.between(checkIn, checkOut);

            PreparedStatement ps = conn.prepareStatement("SELECT valor_diaria FROM quartos WHERE id = ?");
            ps.setInt(1, idQuarto);
            ResultSet rs = ps.executeQuery();
            rs.next();
            double diaria = rs.getDouble("valor_diaria");
            rs.close();
            ps.close();

            double total = diaria * dias;
            for(int i = 0; i < checkBoxesServicos.size(); i++) {
                if(checkBoxesServicos.get(i).isSelected()) {
                    total += servicos.get(i).getPreco();
                }
            }

           int confirm  = JOptionPane.showConfirmDialog(this, String.format("Total: R$ %.2f\nConfirmar?", total));
            if(confirm == JOptionPane.YES_OPTION) {
                ps = conn.prepareStatement("INSERT INTO reservas (id_quarto, id_cliente, data_checkin, data_checkout, valor_total) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, idQuarto);
                ps.setInt(2, idClienteLogado);
                ps.setDate(3, Date.valueOf(checkIn));
                ps.setDate(4, Date.valueOf(checkOut));
                ps.setDouble(5, total);
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                int idReserva = keys.getInt(1);
                keys.close();
                ps.close();

                ps = conn.prepareStatement("INSERT INTO pagamentos (id_reserva, valor, data_pagamento, metodo) VALUES (?, ?, CURRENT_DATE(), 'Cartão')");
                ps.setInt(1, idReserva);
                ps.setDouble(2, total);
                ps.executeUpdate();
                ps.close();

                ps = conn.prepareStatement("UPDATE quartos SET situacao = 'Ocupado' WHERE id = ?");
                ps.setInt(1, idQuarto);
                ps.executeUpdate();
                ps.close();

                JOptionPane.showMessageDialog(this, "Reserva confirmada com sucesso!");
                carregarQuartosDisponiveis();
            }

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro na reserva: " + ex.getMessage());
        }
    }

    private void mostrarTelaCancelarReserva() {
        getContentPane().removeAll();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5,5,5,5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> cbReservas = new JComboBox<>();
        List<Integer> ids = new ArrayList<>();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT r.id, q.numero, r.data_checkin, r.data_checkout, r.id_quarto FROM reservas r JOIN quartos q ON r.id_quarto = q.id WHERE id_cliente = ?");
            ps.setInt(1, idClienteLogado);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int id = rs.getInt("id");
                int numero = rs.getInt("numero");
                String in = rs.getDate("data_checkin").toString();
                String out = rs.getDate("data_checkout").toString();
                cbReservas.addItem("Reserva #" + id + " - Quarto " + numero + " (" + in + " a " + out + ")");
                ids.add(id);
            }
            rs.close();
            ps.close();
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar reservas: " + ex.getMessage());
        }

        if(cbReservas.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Nenhuma reserva encontrada.");
            mostrarTelaReserva();
            return;
        }

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Selecione uma reserva para cancelar:"), c);
        c.gridy = 1;
        panel.add(cbReservas, c);

        JButton btnCancelar = new JButton("Confirmar Cancelamento");
        c.gridy = 2;
        panel.add(btnCancelar, c);

        btnCancelar.addActionListener(e -> {
            int index = cbReservas.getSelectedIndex();
            if(index >= 0) {
                int idReserva = ids.get(index);
                try {
                    PreparedStatement ps = conn.prepareStatement("SELECT id_quarto FROM reservas WHERE id = ?");
                    ps.setInt(1, idReserva);
                    ResultSet rs = ps.executeQuery();
                    int idQuarto = rs.next() ? rs.getInt("id_quarto") : -1;
                    rs.close();
                    ps.close();

                    if(idQuarto != -1) {
                        ps = conn.prepareStatement("DELETE FROM pagamentos WHERE id_reserva = ?");
                        ps.setInt(1, idReserva);
                        ps.executeUpdate();
                        ps.close();

                        ps = conn.prepareStatement("DELETE FROM reservas WHERE id = ?");
                        ps.setInt(1, idReserva);
                        ps.executeUpdate();
                        ps.close();

                        ps = conn.prepareStatement("UPDATE quartos SET situacao = 'Disponível' WHERE id = ?");
                        ps.setInt(1, idQuarto);
                        ps.executeUpdate();
                        ps.close();

                        JOptionPane.showMessageDialog(this, "Reserva cancelada com sucesso!");
                        mostrarTelaCancelarReserva();
                    }
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao cancelar: " + ex.getMessage());
                }
            }
        });

        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SistemaReservaHotel().setVisible(true);
        });
    }

    private static class ServicoExtra {
        private String nome;
        private String descricao;
        private double preco;

        public ServicoExtra(String nome, String descricao, double preco) {
            this.nome = nome;
            this.descricao = descricao;
            this.preco = preco;
        }

        public double getPreco() {
            return preco;
        }

        @Override
        public String toString() {
            return nome + " - R$" + preco + " (" + descricao + ")";
        }
    }
}