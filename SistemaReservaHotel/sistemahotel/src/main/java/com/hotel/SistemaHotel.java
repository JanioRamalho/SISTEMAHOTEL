package com.hotel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SistemaHotel extends JFrame {
    private Connection connection;
    private int idClienteLogado = -1, idReservaAtual = -1, idQuartoSelecionado = -1;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout), painelQuartos;
    private JTextField
        txtNomeCliente   = new JTextField(),
        txtEmailCliente  = new JTextField(),
        txtSenhaCliente  = new JTextField(),
        txtNomeFunc      = new JTextField(),
        txtLoginFunc     = new JTextField(),
        txtSenhaFunc     = new JTextField(),
        txtEmailLogin    = new JTextField(),
        txtSenhaLogin    = new JTextField(),
        txtCheckin       = new JTextField(),
        txtCheckout      = new JTextField();
    private JCheckBox servicoWifi = new JCheckBox("WiFi - R$ 15,00"),
                      servicoCafe = new JCheckBox("Café da manhã - R$ 30,00");
    private double valorServicos = 0;
    private JComboBox<String> comboPagamento = new JComboBox<>(new String[]{"Dinheiro","Cartão","Pix"});

    public SistemaHotel() {
        try {
            connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sistema_hotel", "root", "Jrl-122133");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro BD: " + e.getMessage());
            System.exit(1);
        }

        setTitle("Sistema Simples Hotel");
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel.add(menu(),            "menu");
        mainPanel.add(cadClientePanel(),  "cadCliente");
        mainPanel.add(cadFuncPanel(),     "cadFunc");
        mainPanel.add(loginClientePanel(),"loginCliente");
        mainPanel.add(datesPanel(),       "datas");
        mainPanel.add(quartosPanel(),     "quartos");
        mainPanel.add(servicosPanel(),    "servicos");
        mainPanel.add(pagamentoPanel(),   "pagamento");

        add(mainPanel);
        cardLayout.show(mainPanel, "menu");
    }

    private JPanel menu() {
        JPanel p = new JPanel(new GridLayout(4,1,10,10));
        JButton[] btns = {
            new JButton("Cadastrar Cliente"),
            new JButton("Cadastrar Funcionário"),
            new JButton("Login Cliente"),
            new JButton("Sair")
        };
        btns[0].addActionListener(e -> cardLayout.show(mainPanel, "cadCliente"));
        btns[1].addActionListener(e -> cardLayout.show(mainPanel, "cadFunc"));
        btns[2].addActionListener(e -> cardLayout.show(mainPanel, "loginCliente"));
        btns[3].addActionListener(e -> System.exit(0));
        for (JButton b : btns) p.add(b);
        return p;
    }

    private JPanel cadClientePanel() {
        JPanel p = new JPanel(new GridLayout(4,2,5,5));
        p.add(new JLabel("Nome:"));  p.add(txtNomeCliente);
        p.add(new JLabel("Email:")); p.add(txtEmailCliente);
        p.add(new JLabel("Senha:")); p.add(txtSenhaCliente);
        JButton ok = new JButton("Cadastrar"), vol = new JButton("Voltar");
        ok.addActionListener(e -> { try { cadastrarCliente(); } catch (SQLException ex) { msg("Erro: "+ex); } });
        vol.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        p.add(ok); p.add(vol);
        return p;
    }

    private void cadastrarCliente() throws SQLException {
        String n=txtNomeCliente.getText().trim(), e=txtEmailCliente.getText().trim(), s=txtSenhaCliente.getText().trim();
        if (n.isEmpty()||e.isEmpty()||s.isEmpty()){ msg("Preencha todos os campos!"); return; }
        if (queryInt("SELECT COUNT(*) FROM clientes WHERE email=?", e)>0){ msg("Email já cadastrado!"); return; }
        exec("INSERT INTO clientes(nome,email,senha) VALUES(?,?,?)", n,e,s);
        msg("Cliente cadastrado!"); cardLayout.show(mainPanel, "menu");
    }

    private JPanel cadFuncPanel() {
        JPanel p = new JPanel(new GridLayout(4,2,5,5));
        p.add(new JLabel("Nome:"));  p.add(txtNomeFunc);
        p.add(new JLabel("Login:")); p.add(txtLoginFunc);
        p.add(new JLabel("Senha:")); p.add(txtSenhaFunc);
        JButton ok = new JButton("Cadastrar"), vol = new JButton("Voltar");
        ok.addActionListener(e -> { try { cadastrarFuncionario(); } catch (SQLException ex) { msg("Erro: "+ex); } });
        vol.addActionListener(e -> cardLayout.show(mainPanel, "menu"));
        p.add(ok); p.add(vol);
        return p;
    }

    private void cadastrarFuncionario() throws SQLException {
        String n=txtNomeFunc.getText().trim(), l=txtLoginFunc.getText().trim(), s=txtSenhaFunc.getText().trim();
        if (n.isEmpty()||l.isEmpty()||s.isEmpty()){ msg("Preencha todos os campos!"); return; }
        if (queryInt("SELECT COUNT(*) FROM funcionarios WHERE login=?", l)>0){ msg("Login já existe!"); return; }
        exec("INSERT INTO funcionarios(nome,login,senha) VALUES(?,?,?)", n,l,s);
        msg("Funcionário cadastrado!"); cardLayout.show(mainPanel, "menu");
    }

    private JPanel loginClientePanel() {
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.add(new JLabel("Email:")); p.add(txtEmailLogin);
        p.add(new JLabel("Senha:")); p.add(txtSenhaLogin);
        JButton ok = new JButton("Entrar"), vol = new JButton("Voltar");
        ok.addActionListener(e -> { try { loginCliente(); } catch(SQLException ex){ msg("Erro: "+ex);} });
        vol.addActionListener(e -> cardLayout.show(mainPanel,"menu"));
        p.add(ok); p.add(vol);
        return p;
    }

    private void loginCliente() throws SQLException {
        String e=txtEmailLogin.getText().trim(), s=txtSenhaLogin.getText().trim();
        if (e.isEmpty()||s.isEmpty()){ msg("Preencha email e senha!"); return; }
        PreparedStatement ps = connection.prepareStatement("SELECT id FROM clientes WHERE email=? AND senha=?");
        ps.setString(1,e); ps.setString(2,s);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){ idClienteLogado = rs.getInt(1); msg("Login feito!"); cardLayout.show(mainPanel,"datas"); }
        else msg("Email ou senha inválidos!");
    }

    private JPanel datesPanel() {
        JPanel p = new JPanel(new GridLayout(3,2,5,5));
        p.add(new JLabel("Check-in (yyyy-mm-dd):")); p.add(txtCheckin);
        p.add(new JLabel("Check-out:"));           p.add(txtCheckout);
        JButton ok = new JButton("Buscar Quartos"), vol = new JButton("Voltar");
        ok.addActionListener(e -> {
            if (txtCheckin.getText().isEmpty()||txtCheckout.getText().isEmpty()){ msg("Preencha as datas!"); return; }
            carregarQuartos(txtCheckin.getText(), txtCheckout.getText());
            cardLayout.show(mainPanel,"quartos");
        });
        vol.addActionListener(e -> cardLayout.show(mainPanel,"menu"));
        p.add(ok); p.add(vol);
        return p;
    }

    private JPanel quartosPanel() {
        JPanel p = new JPanel(new BorderLayout());
        painelQuartos.removeAll();
        p.add(new JScrollPane(painelQuartos), BorderLayout.CENTER);
        JPanel b = new JPanel();
        JButton sel = new JButton("Selecionar Quarto"), vol = new JButton("Voltar");
        sel.addActionListener(e -> {
            if (idQuartoSelecionado<0) msg("Selecione um quarto!"); else cardLayout.show(mainPanel,"servicos");
        });
        vol.addActionListener(e -> cardLayout.show(mainPanel,"datas"));
        b.add(sel); b.add(vol);
        p.add(b, BorderLayout.SOUTH);
        return p;
    }

    private void carregarQuartos(String ci, String co) {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT id,numero,tipo,valor_diaria FROM quartos WHERE status='Disponível' AND id NOT IN (" +
                "SELECT id_quarto FROM reservas WHERE NOT (data_checkout<=? OR data_checkin>=?))")) {
            ps.setString(1, ci);
            ps.setString(2, co);
            ResultSet rs = ps.executeQuery();
            ButtonGroup bg = new ButtonGroup();
            while (rs.next()) {
                int id = rs.getInt("id");
                String txt = "Quarto " + rs.getString("numero") +
                             " - " + rs.getString("tipo") +
                             " - R$ " + rs.getDouble("valor_diaria");
                JRadioButton rb = new JRadioButton(txt);
                rb.addActionListener(e -> idQuartoSelecionado = id);
                bg.add(rb);
                painelQuartos.add(rb);
            }
            painelQuartos.revalidate();
            if (painelQuartos.getComponentCount() == 0) {
                msg("Nenhum quarto disponível nesse período.");
                cardLayout.show(mainPanel, "datas");
            }
        } catch (SQLException e) {
            msg("Erro ao carregar quartos: " + e.getMessage());
        }
    }

    private JPanel servicosPanel() {
        JPanel p = new JPanel(new GridLayout(3,1,5,5));
        JButton ok = new JButton("Continuar"), vol = new JButton("Voltar");
        ok.addActionListener(e -> {
            valorServicos = (servicoWifi.isSelected()?15:0) + (servicoCafe.isSelected()?30:0);
            salvarReservaEMostrarPagamento();
        });
        vol.addActionListener(e -> cardLayout.show(mainPanel,"quartos"));
        p.add(servicoWifi); p.add(servicoCafe); p.add(ok); p.add(vol);
        return p;
    }

    private void salvarReservaEMostrarPagamento() {
        try {
            double diaria = queryDouble("SELECT valor_diaria FROM quartos WHERE id=?", idQuartoSelecionado);
            long dias = ChronoUnit.DAYS.between(
                LocalDate.parse(txtCheckin.getText()), LocalDate.parse(txtCheckout.getText()));
            if (dias <= 0) { msg("Datas inválidas!"); return; }
            double total = diaria * dias + valorServicos;
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO reservas(id_quarto,id_cliente,data_checkin,data_checkout,valor_total) " +
                "VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idQuartoSelecionado);
            ps.setInt(2, idClienteLogado);
            ps.setString(3, txtCheckin.getText());
            ps.setString(4, txtCheckout.getText());
            ps.setDouble(5, total);
            ps.executeUpdate();
            ResultSet rk = ps.getGeneratedKeys();
            if (rk.next()) idReservaAtual = rk.getInt(1);
            msg(String.format("Reserva criada!%nTotal a pagar: R$ %.2f", total));
            cardLayout.show(mainPanel, "pagamento");
        } catch (SQLException e) {
            msg("Erro ao salvar reserva: " + e.getMessage());
        }
    }

    private JPanel pagamentoPanel() {
        JPanel p = new JPanel(new GridLayout(3,1,5,5));
        JButton ok = new JButton("Pagar"), vol = new JButton("Voltar");
        ok.addActionListener(e -> {
            try { realizarPagamento(); }
            catch (SQLException ex) { msg("Erro no pagamento: " + ex.getMessage()); }
        });
        vol.addActionListener(e -> cardLayout.show(mainPanel,"servicos"));
        p.add(comboPagamento); p.add(ok); p.add(vol);
        return p;
    }

    private void realizarPagamento() throws SQLException {
        double total = queryDouble("SELECT valor_total FROM reservas WHERE id=?", idReservaAtual);
        exec("INSERT INTO pagamentos(id_reserva,valor,data_pagamento,metodo) VALUES(?,?,NOW(),?)",
             idReservaAtual, total, comboPagamento.getSelectedItem());
        exec("UPDATE quartos SET status='Ocupado' WHERE id=?", idQuartoSelecionado);
        msg("Pagamento efetuado com sucesso! Reserva confirmada.");
        cardLayout.show(mainPanel,"menu");
    }

    // helpers
    private int queryInt(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i+1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()? rs.getInt(1) : 0;
            }
        }
    }

    private double queryDouble(String sql, Object... params) throws SQLException {
        return queryInt(sql, params);
    }

    private void exec(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setObject(i+1, params[i]);
            ps.executeUpdate();
        }
    }

    private void msg(String m) { JOptionPane.showMessageDialog(this, m); }

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(() -> {
            new SistemaHotel().setVisible(true);
        });
    }
}
