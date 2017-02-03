<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
							  xmlns:fo="http://www.w3.org/1999/XSL/Format" 
							  xmlns:rms1="http://www.versaterm.com/ticketget"
							  xmlns:rms2="http://www.versaterm.com/ticket"
                xmlns:rms3="http://www.versaterm.com/rms/person"
                xmlns:rms4="http://www.versaterm.com/rms/per_detail"
							  exclude-result-prefixes="fo">
  <xsl:output method="xml" version="1.0" omit-xml-declaration="no" indent="yes"/>
  <xsl:param name="versionParam" select="'1.0'"/> 
  <!-- ========================= -->
  <!-- root element: projectteam -->
  <!-- ========================= -->
  <xsl:template match="/">
    <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
      <fo:layout-master-set>
        <fo:simple-page-master master-name="simpleA4" page-height="29.7cm" page-width="21cm" margin-top="2cm" margin-bottom="2cm" margin-left="2cm" margin-right="2cm">
          <fo:region-body/>
        </fo:simple-page-master>
      </fo:layout-master-set>
      <fo:page-sequence master-reference="simpleA4">
        <fo:flow flow-name="xsl-region-body">
          <fo:block font-size="16pt" font-weight="bold" space-after="5mm">Traffic Ticket Number: <xsl:value-of select="rms1:vdxTicket_getKeyResponseElement//rms2:TICKET_NUM"/>
          </fo:block>
          <fo:block font-size="12pt" font-weight="bold" space-after="5mm">Offendee <xsl:value-of select="$versionParam"/>
          </fo:block>

          <xsl:for-each select="rms1:vdxTicket_getKeyResponseElement//rms2:PERSON_DATA">
              <fo:block font-size="10pt" font-weight="bold" space-before="3mm" space-after="1mm">  
                <xsl:value-of select="rms3:ENTITY_SYNOPSIS"/>
              </fo:block>
              <fo:block font-size="10pt" space-after="1mm">  
                <xsl:value-of select="rms3:ROLE_TRANS"/>
              </fo:block>              
              <fo:block font-size="10pt">
                <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                  <fo:table-column column-width="4cm"/>
                  <fo:table-column column-width="4cm"/>
                  <fo:table-column column-width="5cm"/>
                  <fo:table-body>
                    <xsl:apply-templates select="rms3:CASE_SPECIFIC/rms4:ADDRESS"/>
                  </fo:table-body>
                </fo:table>
              </fo:block>                        
          </xsl:for-each>
          <fo:block font-size="12pt" font-weight="bold" space-before="5mm" space-after="3mm">Charges <xsl:value-of select="$versionParam"/>
          </fo:block>          
           <fo:block font-size="12pt" font-weight="bold" space-before="5mm" space-after="3mm">Ticket Information <xsl:value-of select="$versionParam"/>
          </fo:block>         
          
          
          
          
          <fo:block font-size="10pt">
            <fo:table table-layout="fixed" width="100%" border-collapse="separate">
              <fo:table-column column-width="4cm"/>
              <fo:table-column column-width="4cm"/>
              <fo:table-column column-width="5cm"/>
              <fo:table-body>
                <xsl:apply-templates select="rms1:vdxTicket_getKeyResponseElement//rms2:NARRATIVES_DATA"/>
              </fo:table-body>
            </fo:table>
          </fo:block>
        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
  <!-- ========================= -->
  <!-- child element: member     -->
  <!-- ========================= -->
  <xsl:template match="rms2:NARRATIVES_DATA">
    <fo:table-row>
      <fo:table-cell>
        <fo:block>
          <xsl:value-of select="NARTEXT/LINE"/>
        </fo:block>
      </fo:table-cell>
     </fo:table-row>
  </xsl:template>

  <xsl:template match="rms4:ADDRESS">
    <fo:table-row>
      <fo:table-cell>
        <fo:block>
          <xsl:value-of select="concat(STREET_DETAILS/STREET_NUMBER,' ',STREET_DETAILS/DIRECTION,' ',STREET_DETAILS/STREET_NAME,' ',STREET_DETAILS/STREET_TYPE)"/>
        </fo:block>
      </fo:table-cell>
     </fo:table-row>
  </xsl:template>  
  
</xsl:stylesheet>



