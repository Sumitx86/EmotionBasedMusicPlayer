package Expressionrecognition;

import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import Luxand.*;
import Luxand.FSDK.*;
import Luxand.FSDKCam.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import javazoom.jl.player.Player;
import java.sql.*;

/**
 * The application's main frame.
 */
public class ExpressionRecognitionView extends FrameView {
    int index=0;
                    
    public ExpressionRecognitionView(SingleFrameApplication app) {
        super(app);

        initComponents();
        
        final JPanel mainFrame = this.mainPanel;
       /* Library Activation*/
        try {
            int r = FSDK.ActivateLibrary("iS6BbLSW095/b/4oMnQelB72Guz+0EcMhTGyT4x+lHNFDVCKcUMMxTwtLuZYbEzCvIlUMoUzs6c2Wnuhs2b5AN7RrRq+/rYyU8EFg2uhWI82l7EKZmjAuTktMHexFUpfi7gLZQGWYq/n/yDW1OR9jfWwxPWWU6mHfi1x/27LsTk=");
            if (r != FSDK.FSDKE_OK){
                JOptionPane.showMessageDialog(mainPanel, "Please run the License Key Wizard (Start - Luxand - FaceSDK - License Key Wizard)", "Error activating FaceSDK", JOptionPane.ERROR_MESSAGE); 
                System.exit(r);
            }
        } 
        catch(java.lang.UnsatisfiedLinkError e) {
            JOptionPane.showMessageDialog(mainPanel, e.toString(), "Link Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }    
            
        FSDK.Initialize();
           
       /*Creating a tracker Object  */ 
        final HTracker tracker = new HTracker(); // creating a Tracker
        FSDK.CreateTracker(tracker);
     
        
        int err[] = new int[1];
        err[0] = 0;
        FSDK.SetTrackerMultipleParameters(tracker, "RecognizeFaces=false; DetectExpression=true; HandleArbitraryRotations=false; DetermineFaceRotationAngle=false; InternalResizeWidth=100; FaceDetectionThreshold=5;", err);
        
        // Initialising Camera
        FSDKCam.InitializeCapturing(); // to access camera
                
        // Camera List
        TCameras cameraList = new TCameras();
        int count[] = new int[1];
        FSDKCam.GetCameraList(cameraList, count);
        if (count[0] == 0){
            JOptionPane.showMessageDialog(mainFrame, "Please attach a camera"); 
            System.exit(1);
        }
        
        String cameraName = cameraList.cameras[0];
        FSDK_VideoFormats formatList = new FSDK_VideoFormats();
        FSDKCam.GetVideoFormatList(cameraName, formatList, count);
        FSDKCam.SetVideoFormat(cameraName, formatList.formats[0]);
        
        cameraHandle = new HCamera();
        int r = FSDKCam.OpenVideoCamera(cameraName, cameraHandle);
        if (r != FSDK.FSDKE_OK){
            JOptionPane.showMessageDialog(mainFrame, "Error opening camera"); 
            System.exit(r);
        }
    
        // Timer to draw and process image from camera
        drawingTimer = new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HImage imageHandle = new HImage();
                if (FSDKCam.GrabFrame(cameraHandle, imageHandle) == FSDK.FSDKE_OK){
                    Image awtImage[] = new Image[1];
                    if (FSDK.SaveImageToAWTImage(imageHandle, awtImage, FSDK_IMAGEMODE.FSDK_IMAGE_COLOR_24BIT) == FSDK.FSDKE_OK){
                        
                        BufferedImage bufImage = null;
                        TFacePosition.ByReference facePosition = new TFacePosition.ByReference();
                        
                        long[] IDs = new long[256]; // maximum of 256 faces detected
                        long[] faceCount = new long[1];
                        
                        FSDK.FeedFrame(tracker, 0, imageHandle, faceCount, IDs); 
                        for (int i=0; i<faceCount[0]; ++i) {
                            FSDK.GetTrackerFacePosition(tracker, 0, IDs[i], facePosition);
                            
                            int left = facePosition.xc - (int)(facePosition.w * 0.6);
                            int top = facePosition.yc - (int)(facePosition.w * 0.5);
                            int w = (int)(facePosition.w * 1.2);
                            
                            bufImage = new BufferedImage(awtImage[0].getWidth(null), awtImage[0].getHeight(null), BufferedImage.TYPE_INT_ARGB);
                            Graphics gr = bufImage.getGraphics(); 
                            gr.drawImage(awtImage[0], 0, 0, null);
                            gr.setColor(Color.green);
                            gr.drawRect(left, top, w, w); // draw face rectangle
                            
                            String [] AttributeValues = new String[1];
                            
    			    int res = FSDK.GetTrackerFacialAttribute(tracker, 0, IDs[i], "Expression", AttributeValues, 1024);
			    if (FSDK.FSDKE_OK == res) { // draw Expression
                                float [] ConfidenceSmile = new float[1];
                                float [] ConfidenceEyesOpen = new float[1];
                                FSDK.GetValueConfidence(AttributeValues[0], "Smile", ConfidenceSmile);
                                FSDK.GetValueConfidence(AttributeValues[0], "EyesOpen", ConfidenceEyesOpen);
                                
                                String str = "Smile: " + Integer.toString((int)(ConfidenceSmile[0]*100))
                                             + "%; Eyes open: " + Integer.toString((int)(ConfidenceEyesOpen[0]*100)) + "%";
                                
                                gr.setFont(new Font("Arial", Font.BOLD, 16));
                                FontMetrics fm = gr.getFontMetrics();
                                java.awt.geom.Rectangle2D textRect = fm.getStringBounds(str, gr);
                                gr.drawString(str, (int)(facePosition.xc - textRect.getWidth()/2), (int)(top + w + textRect.getHeight()));
                            
                            /************** SET LAbeling***********/
                                int output =Integer.parseInt(Integer.toString((int)(ConfidenceSmile[0]*100)));
                                //System.out.println("The final Expression : "+ output);
                                jLabel2.setText("Emotion: " + Integer.toString((int)(ConfidenceSmile[0]*100)));
                                jLabel1.setText("Active: " + Integer.toString((int)(ConfidenceEyesOpen[0]*100)));
                              //  jLabel3.setText("Active: " + Integer.toString((int)(ConfidenceEyesOpen[0]*100)));
                                if( output > 0 && output < 5)
                                    { jLabel3.setText(" Sad ");}
                                else if(output > 5 && output <40)
                                    { jLabel3.setText(" Confused ");}
                                else if(output > 40 )
                                    { jLabel3.setText(" Very Happy ");}
                                
                                int count=0;
                                do{
                                if( output > 0 && output < 5)
                                    { 
                                      jLabel4.setIcon(new ImageIcon("F:\\Facial Recognition\\ExpressionRecognition\\Java\\Sad.png"));}
                                else if(output > 5 && output <40)
                                    { 
                                      jLabel4.setIcon(new ImageIcon("F:\\Facial Recognition\\ExpressionRecognition\\Java\\happy.PNG"));}
                                else if(output > 40 )
                                    { 
                                      jLabel4.setIcon(new ImageIcon("F:\\Facial Recognition\\ExpressionRecognition\\Java\\laugh.PNG"));}
                                
                                }while(count > count+1);
                                count++;
                                
                                
                                // Database *******************
                    try
                        {
                            ++index;
                    //System.out.println(" the values for databse is  " +output);
                    Class.forName("oracle.jdbc.OracleDriver");//fully qualified class name
                    Connection con=DriverManager.getConnection("jdbc:oracle:thin:@Jarvis:1521:XE", "medical", "medical");
                    Statement stmt=con.createStatement();
                    	//System.out.println("\n");
        		//System.out.println("****************************");
        		//System.out.println(index);
        		//System.out.println("****************************");
        		//System.out.println("\n");
        		
                        String q = "insert into output values( "+ (index) +" , " +output+ "  )";
			stmt.execute(q);
                      //  System.out.println("data Inserted");
                        String label5= Integer.toString(index);
		        System.out.println("Result " + index);
                        jLabel5.setText("Counter: " + label5);
                        }
		        catch (ClassNotFoundException e2) {
                            System.out.println(e2);
                                } catch (SQLException e2) {
                                    System.out.println(e2);
                                }
                        }}
                        // display current frame
                        mainFrame.getRootPane().getGraphics().drawImage((bufImage != null) ? bufImage : awtImage[0], 0, 0, null);
                    }
                    FSDK.FreeImage(imageHandle); // delete the FaceSDK image handle
                }
            }
        });
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(Expressionrecognition.ExpressionRecognitionApp.class).getContext().getResourceMap(ExpressionRecognitionView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mainPanel.setAlignmentX(1.0F);
        mainPanel.setAlignmentY(1.0F);
        mainPanel.setMaximumSize(new java.awt.Dimension(500, 500));
        mainPanel.setMinimumSize(new java.awt.Dimension(500, 500));
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        mainPanel.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                mainPanelComponentAdded(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(Expressionrecognition.ExpressionRecognitionApp.class).getContext().getActionMap(ExpressionRecognitionView.class, this);
        jButton1.setAction(actionMap.get("buttonStart")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setAutoscrolls(true);
        jPanel1.setName("jPanel1"); // NOI18N

        jLabel2.setBackground(resourceMap.getColor("jLabel2.background")); // NOI18N
        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, resourceMap.getColor("jLabel2.border.highlightColor"), resourceMap.getColor("jLabel2.border.shadowColor"))); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel1.setBackground(resourceMap.getColor("jLabel1.background")); // NOI18N
        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(resourceMap.getString("jLabel1.toolTipText")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel5.setBackground(resourceMap.getColor("jLabel5.background")); // NOI18N
        jLabel5.setFont(resourceMap.getFont("jLabel5.font")); // NOI18N
        jLabel5.setForeground(resourceMap.getColor("jLabel5.foreground")); // NOI18N
        jLabel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel3.setBackground(resourceMap.getColor("jLabel3.background")); // NOI18N
        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap(687, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(31, 31, 31)
                .addComponent(jButton1)
                .addContainerGap())
        );

        setComponent(mainPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void mainPanelComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_mainPanelComponentAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_mainPanelComponentAdded

    @Action
    public void buttonStart() {
        this.jButton1.setEnabled(false);
        drawingTimer.start();
    }
    
    public void closeCamera(){
        FSDKCam.CloseVideoCamera(cameraHandle);
        FSDKCam.FinalizeCapturing();
        FSDK.Finalize();
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
    
    public final Timer drawingTimer;
    private HCamera cameraHandle;
}
