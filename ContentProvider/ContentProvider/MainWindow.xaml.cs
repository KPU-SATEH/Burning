using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Microsoft.Win32;
using eBdb.EpubReader;
using Ionic.Zip;

namespace ContentProvider
{
    /// <summary>
    /// Interaction logic for MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        static int count = 0;
        Epub epub = null;
        string fileFullName = null;
        //string title;
        //string plainText;
        //string htmlText;
        ContentData contentData;
        
        public MainWindow()
        {
            //epub = new Epub(@"c:\Users\ljyok\Desktop\도쿠가와이에야스.epub");
            //title = epub.Title[0];
            //string author = epub.Creator[0];
            //plainText = epub.GetContentAsPlainText();
            //htmlText = epub.GetContentAsHtml();
            //contentData = epub.Content[0] as ContentData;
            //plainText = contentData.GetContentAsPlainText();
            //List<NavPoint> navPoints = epub.TOC;

            //button_back.IsEnabled = false;
            //button_front.IsEnabled = false;
            InitializeComponent();
        }

        private void btnBackShowDlg_Click(object sender, RoutedEventArgs e)
        {
            paraBodyText.Inlines.Clear();
            try
            {
                contentData = epub.Content[--count] as ContentData;
            }
            catch(ArgumentOutOfRangeException ex)
            {
                MessageBox.Show("Its first page!");
                count = 0;
                contentData = epub.Content[count] as ContentData;
            }
            Run str = new Run(contentData.GetContentAsPlainText());
            paraBodyText.Inlines.Add(str);
        }

        private void btnFrontShowDlg_Click(object sender, RoutedEventArgs e)
        {
            paraBodyText.Inlines.Clear();
            try
            {
                contentData = epub.Content[++count] as ContentData;
            }
            catch(ArgumentOutOfRangeException ex)
            {
                MessageBox.Show("Its last page!");
                count--;
                contentData = epub.Content[count] as ContentData;
            }
            Run str = new Run(contentData.GetContentAsPlainText());
            paraBodyText.Inlines.Add(str);
        }

        public string ShowFileOpenDialog()
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Title = "File open";
            openFileDialog.Filter = "e- book (*.epub) | *.epub";

            if (openFileDialog.ShowDialog() == true)
            {
                //button_back.IsEnabled = true;
                //button_front.IsEnabled = true;
                return openFileDialog.FileName;
            }
            else if (openFileDialog.ShowDialog() == false)
            {
                return null;
            }
            return null;
        }

        private void menuNew_Click(object sender, RoutedEventArgs e)
        {

        }

        private void menuOpen_Click(object sender, RoutedEventArgs e)
        {
            fileFullName = ShowFileOpenDialog();
            if (fileFullName != null)
                epub = new Epub(fileFullName);
        }

        private void menuSave_Click(object sender, RoutedEventArgs e)
        {

        }

        private void menuRedo_Click(object sender, RoutedEventArgs e)
        {

        }

        private void menuUndo_Click(object sender, RoutedEventArgs e)
        {

        }
    }
}
