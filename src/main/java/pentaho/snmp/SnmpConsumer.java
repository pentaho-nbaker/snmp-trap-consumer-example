/*
 * This program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 * or from the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2014 Pentaho Corporation. All rights reserved.
 */

package pentaho.snmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nbaker on 9/9/14.
 */
@Path( "/traps" )
public class SnmpConsumer {
  Logger logger = LoggerFactory.getLogger( SnmpConsumer.class );
  LinkedList<Trap> traps = new LinkedList<Trap>();
//
//  {
//    Trap t = new Trap();
//    t.setOID( "1.2.4.5" );
//    Variable variable = new Variable();
//    variable.setOid( "1.2.4.5.6" );
//    variable.setValue( "Testing" );
//    t.variables.add( variable );
//    traps.add( t );
//    t = new Trap();
//    t.setOID( "1.2.4.5.8" );
//    traps.add( t );
//  }

  @Path( "/list" )
  @GET
  @Produces( "application/json" )
  public List<Trap> getList() {
    return traps;
  }


  public void receive( String s ) {
    logger.info( "Received Trap!" );
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {

      Trap t = new Trap();


      Document document = factory.newDocumentBuilder().parse( new ByteArrayInputStream( s.getBytes( "UTF-8" ) ) );
      NodeList entries = document.getElementsByTagName( "entry" );
      for ( int i = 0; i < entries.getLength(); i++ ) {
        String oid = entries.item( i ).getChildNodes().item( 0 ).getTextContent();
        String value = entries.item( i ).getChildNodes().item( 1 ).getTextContent();

        if ( oid.equals( "1.3.6.1.6.3.1.1.4.1.0" ) ) {
          t.setOID( value );
        } else {
          Variable variable = new Variable();
          variable.setOid( oid );
          variable.setValue( value );
          t.variables.add( variable );
        }

      }
      traps.push( t );
      if ( traps.size() > 8 ) {
        traps.removeLast();
      }
    } catch ( SAXException e ) {
      e.printStackTrace();
    } catch ( IOException e ) {
      e.printStackTrace();
    } catch ( ParserConfigurationException e ) {
      e.printStackTrace();
    }
  }
}
