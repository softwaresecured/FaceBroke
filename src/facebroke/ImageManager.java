package facebroke;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import facebroke.model.Image;
import facebroke.model.User;
import facebroke.util.HibernateUtility;


@WebServlet("/image")
@MultipartConfig
public class ImageManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(ImageManager.class);
	private DiskFileItemFactory factory;
       
    public ImageManager() {
        super();
    }
    
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	log.info("Received GET request");
    	
    	if(factory==null) {
			buildFactory();
		}
    	
    	Session sess = HibernateUtility.getSessionFactory().openSession();
		
    	String id_string = req.getParameter("id");
    	
    	try {
    		long id = Long.parseLong(id_string);
    		
    		Image img = (Image) sess.createQuery("FROM Image i WHERE i.id=:id").setParameter("id", id).list().get(0);
    		res.setContentLength(img.getSize());
    		res.getOutputStream().write(img.getContent());
    		
    		
    	}catch(NumberFormatException e) {
    		log.error(e.getMessage());
    	}catch(IndexOutOfBoundsException e) {
    		log.error("No such image. Sending dummy");
    		req.getRequestDispatcher("resources/img/dummy.png").forward(req, res);
    	}
    	
    	sess.close();
	}

    
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		log.info("Received POST request");
		
		if(factory==null) {
			buildFactory();
		}
		
		String owner_id_string = "";
		String creator_id_string = "";
		String label = "";
		String mimetype = "";
		
		try {
			List<FileItem> items = new ServletFileUpload(factory).parseRequest(req);
			int size = -1;
			byte[] data = null;
			
			
			for(FileItem i : items) {
				String name = i.getFieldName();
				String val = i.getString();
				
				switch (name) {
					case "owner_id":
						owner_id_string = val;
						break;
						
					case "creator_id":
						creator_id_string = val;
						break;
						
					case "label":
						label = val;
						break;
	
					default:
						break;
				}
				
				if(i.isFormField()) {
					log.info("Field: "+name+"    Val: "+val);
				}else {
					log.info("Size: "+i.getSize());
					data = i.get();
					size = (int) i.getSize();
				}
			}
			
			if(data==null) {
				// HANDLE BAD REQUEST
			}
			
			Session sess = HibernateUtility.getSessionFactory().openSession();
			
			User owner, creator;
			
			owner = (User)sess.createQuery("From User u WHERE u.id=:user_id")
								.setParameter("user_id", Long.parseLong(owner_id_string))
								.list()
								.get(0);
			
			creator = (User)sess.createQuery("From User u WHERE u.id=:user_id")
					.setParameter("user_id", Long.parseLong(creator_id_string))
					.list()
					.get(0);
			
			sess.beginTransaction();
			Image img = new Image(owner, creator, Image.Viewable.All, data, size, label);
			sess.save(img);
			sess.getTransaction().commit();
			log.info("Created new img: "+img.toString());
			log.info("Mimetype: "+mimetype);
			
		}catch(FileUploadException e) {
			log.error(e.getMessage());
		}catch(NumberFormatException e) {
			log.error(e.getMessage());
		}
	}

	
	private void buildFactory() {
		factory = new DiskFileItemFactory();
		
		ServletContext ctx = this.getServletConfig().getServletContext();
		File repo = (File) ctx.getAttribute("javax.servlet.context.tempdir");
		factory.setRepository(repo);
	}
}
