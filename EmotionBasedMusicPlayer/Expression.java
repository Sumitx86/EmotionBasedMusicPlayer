package expressionrecognition;

public class Expression extends javax.swing.JFrame {
    
    public Expression() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        button1 = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(Expressionrecognition.ExpressionRecognitionApp.class).getContext().getResourceMap(Expression.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setForeground(resourceMap.getColor("Form.foreground")); // NOI18N
        setMaximumSize(new java.awt.Dimension(1300, 700));
        setMinimumSize(new java.awt.Dimension(1300, 700));
        setName("Form"); // NOI18N
        setSize(new java.awt.Dimension(1300, 700));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setIcon(resourceMap.getIcon("jLabel1.icon")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, resourceMap.getColor("jLabel1.border.highlightOuterColor"), resourceMap.getColor("jLabel1.border.highlightInnerColor"), resourceMap.getColor("jLabel1.border.shadowOuterColor"), resourceMap.getColor("jLabel1.border.shadowInnerColor"))); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 100));
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setOpaque(true);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 570, 450));

        button1.setBackground(resourceMap.getColor("Start.background")); // NOI18N
        button1.setFont(resourceMap.getFont("Start.font")); // NOI18N
        button1.setForeground(resourceMap.getColor("Start.foreground")); // NOI18N
        button1.setLabel(resourceMap.getString("Start.label")); // NOI18N
        button1.setName("Start"); // NOI18N
        getContentPane().add(button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 530, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
             java.awt.EventQueue.invokeLater(new Runnable() {
             public void run() {
                new Expression().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
