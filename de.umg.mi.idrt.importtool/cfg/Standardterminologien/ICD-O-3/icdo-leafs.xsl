<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
  The idea of this script is to transform existing XML files in ClaML into 
  XML files in a format that can be easily parsed by Talend's XML 
  components. Additionally, it contains only those attributes that are
  relevant for i2b2's ontology model.
  
  Typically, the destination file consists soly of <class> elements that 
  represent concepts with four inner elements:
    - code:	 the unique identifier given to a concept in a medical 
	         classification (f.i. ICD-10 code), used as a value in clinical
			 data
	- label: the human readable designation given to a concept in a medical 
	         classification
	- level: the numeric value of the layer in the nested hierarchy of 
	         concepts in the i2b2 ontology cell
	- path:  an i2b2-specific absolute path in the hierarchy of concepts in
	         the i2b2 ontology cell
  
  The order of the inner elements is doesn't make any difference, but the 
  two templates generating <level> und <path> must address the same entities
  (all inner elements are required for a valid i2b2 navigation entry).
  
  An example snippet looks like:
  
  	<class>
		<level>2</level>
		<path>\i2b2\ST\TNM\Metastasis</path>
		<label>Fernmetastasen\</label>
		<code>M</code>
	</class>
-->

<!-- @todo:
	- provide variable for root element name
	- provide variable for i2b2 path prefix
	- normalize level accross different style sheets
	- generate root element for this classification in this file
	- provide a mechanismn for user-based selection as i2b2 leafs or nodes
-->
<!-- *************************** main template *** -->
<xsl:template match="/">

  <!-- xsl:text instructions provide nice formatting for generated code -->
  <xsl:text>&#xA;</xsl:text><icdo><xsl:text>&#xA;</xsl:text>

  <!-- Only <Class> elements of kind 'category' are parsed into concepts -->
	  <xsl:for-each select="//Class[@kind='category']"><!-- //Class[not(./SubClass)] -->

      <!-- Add surrounding <class> element -->
      <xsl:text>&#9;</xsl:text><class><xsl:text>&#xA;</xsl:text>

	    <!-- Add <code> element-->
		<xsl:text>&#9;&#9;</xsl:text><code><xsl:value-of select="@code"/></code><xsl:text>&#xA;</xsl:text>

	    <!-- Add <label> element-->
        <xsl:text>&#9;&#9;</xsl:text><label><xsl:value-of select="Rubric[@kind='preferred']/Label"/></label><xsl:text>&#xA;</xsl:text>

	    <!-- Add <path> element-->
        <xsl:text>&#9;&#9;</xsl:text><path>

		  <!-- Write path prefix and call template "i2b2path"-->
          <xsl:text>\i2b2\ST\ICD-O</xsl:text>
		  <xsl:call-template name="i2b2path">
            <xsl:with-param name="code" select="@code"/>
          </xsl:call-template>
          <xsl:text>\</xsl:text>
	    </path><xsl:text>&#xA;</xsl:text>

	    <!-- Add <level> element-->
        <xsl:text>&#9;&#9;</xsl:text><level>

		<!-- Call template "i2b2level"-->
		  <xsl:call-template name="i2b2level">
            <xsl:with-param name="code" select="@code"/>
            <xsl:with-param name="level" select="4"/>
          </xsl:call-template>
	    </level><xsl:text>&#xA;</xsl:text>

	    <!-- Add </code> element-->
		<xsl:text>&#9;</xsl:text></class><xsl:text>&#xA;</xsl:text>
      </xsl:for-each>
  </icdo><xsl:text>&#xA;</xsl:text>
</xsl:template>

<!-- *************************** template name="i2b2path" *** -->
<!--
  The <path> is constructed of a classification-specific manual prefix and a 
  sequence of concept codes reflecting the navigation/structure of the 
  ontology separated by backslashes.
  
  This template takes the code of the current concept and looks for its 
  superclass, recursively calling this template with the code of the higher 
  level concept. When the root concept is reached, the recursion stops and 
  the path from root to the inital element is printed.
-->
<xsl:template name="i2b2path">
  <xsl:param name="code" select="'Undefined'"/>
  
  <!-- If there is a code and the current element has a super class (meaning
  the concept is not the root of the hierarchy), the template calls itself 
  with the code of the super class recursively -->
  <xsl:if test="$code!=''">
    <xsl:if test="SuperClass">
      <xsl:call-template name="i2b2path">
          <xsl:with-param name="code" select="//Class[@code=$code]/SuperClass/@code"/>
      </xsl:call-template>
    </xsl:if>

    <!-- Printing of all codes separated by backslashes starting from the root element
	until the element which initially started the template recursion-->
	<xsl:text>\</xsl:text>
    <xsl:value-of select="$code"/>
  </xsl:if>  
</xsl:template>

<!-- *************************** template name="i2b2level" *** -->
<!--
  The <level> can be interpreted as the depth of a concept in the navigation 
  hierarchy within i2b2. This template takes the code of the current concept 
  and looks for its superclass, recursively calling this template with the 
  code of the next higher level concept. The number of recursions until the 
  root concept is reached will be counted in the level parameter. When the 
  root concept is reached, the recursion stops and the level parameter is 
  printed.
-->
<xsl:template name="i2b2level">
  
  <!-- the 'select' values are just initialization parameters and never used-->
  <xsl:param name="code" select="'Undefined'"/>
  <xsl:param name="level" select="100"/>
  
  <xsl:choose>
  
    <!-- If there is a code and the current element has a super class 
	(meaning the concept is not the root of the hierarchy), the template 
	calls itself with the code of the super class recursively -->
    <xsl:when test="$code!=''">
      <xsl:if test="SuperClass">
        <xsl:call-template name="i2b2level">
            <xsl:with-param name="code" select="//Class[@code=$code]/SuperClass/@code"/>
		    <xsl:with-param name="level" select="$level + 1"/>
        </xsl:call-template>
      </xsl:if>
	  
      <!-- Printing the level parameter -->
	  <xsl:if test="//Class[@code=$code][not(SuperClass)]">
        <xsl:value-of select="$level"/>
      </xsl:if>
    </xsl:when>  
    <xsl:otherwise>  
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
