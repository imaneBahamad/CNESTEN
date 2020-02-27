package cnesten.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cnesten.dao.ActionDAO;

@WebServlet( "/downloadCheck" )
public class DownloadCheck extends HttpServlet {
    public static final String PARAM_ID            = "id";
    private static final int   TAILLE_TAMPON       = 10240;
    private static final int   DEFAULT_BUFFER_SIZE = 10240;          // 10 ko
    public static final String VUE                 = "/listeActions";

    private static final long  serialVersionUID    = 1L;

    // Injection de nos EJBs (Session Bean Stateless)
    @EJB
    private ActionDAO          actionDao;

    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {
        /* R�cup�ration du param�tre */
        Long id = Long.parseLong( getValeurParametre( request, PARAM_ID ) );

        byte[] bt = actionDao.get_check( id );

        /* Pr�pare les flux */
        BufferedInputStream entree = null;
        BufferedOutputStream sortie = null;
        try {
            /* Ouvre les flux */
            // entree = (BufferedInputStream) getStreamfromByte( bt );
            entree = new BufferedInputStream( getStreamfromByte( bt ), TAILLE_TAMPON );
            sortie = new BufferedOutputStream( response.getOutputStream(), TAILLE_TAMPON );

            /* Lit le fichier et �crit son contenu dans la r�ponse HTTP */
            byte[] tampon = new byte[TAILLE_TAMPON];
            int longueur;
            while ( ( longueur = entree.read( tampon ) ) > 0 ) {
                sortie.write( tampon, 0, longueur );
            }
        } finally {
            try {
                sortie.close();
            } catch ( IOException ignore ) {
            }
            try {
                entree.close();
            } catch ( IOException ignore ) {
            }
        }

        /* Redirection vers l'accueil */
        response.sendRedirect( request.getContextPath() + VUE );
    }

    protected void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException {

    }

    public InputStream getStreamfromByte( byte[] bt ) throws IOException {
        InputStream targetStream = new ByteArrayInputStream( bt );
        return targetStream;
    }

    /*
     * M�thode utilitaire qui retourne null si un param�tre est vide, et son
     * contenu sinon.
     */
    private static String getValeurParametre( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }

}
