using System;
using System.IO;
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
        double originalWidth, originalHeight;
        string eraserToUse = null;


        public MainWindow()
        {
            InitializeComponent();

            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Ink;

            this.Loaded += new RoutedEventHandler(MainWindow_Loaded);
            
            button_back.IsEnabled = false;
            button_front.IsEnabled = false;
        }

        private void MainWindow_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            ChangeSize(e.NewSize.Width, e.NewSize.Height);
        }

        private void MainWindow_Loaded(object sender, RoutedEventArgs e)
        {
            originalWidth = this.Width;
            originalHeight = this.Height;

            if (this.WindowState == WindowState.Maximized)
            {
                ChangeSize(this.ActualWidth, this.ActualHeight);
            }

            this.SizeChanged += new SizeChangedEventHandler(MainWindow_SizeChanged);
        }

        private void ChangeSize(double width, double height)
        {
            myDocumentReader.Width = width / 2;
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
            openFileDialog.Filter = "e-book (*.epub) | *.epub";

            if(openFileDialog.ShowDialog() == true)
            {
                button_back.IsEnabled = true;
                button_front.IsEnabled = true;

                return openFileDialog.FileName;
            }

            return null;
        }

        public void ShowFileSaveDialog()
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();
            saveFileDialog.Filter = "e-book (*.epub) | *.epub";

            if(saveFileDialog.ShowDialog() == true)
            {
                //File.WriteAllBytes(saveFileDialog.FileName);
                MessageBox.Show(saveFileDialog.FileName);
            }
        }

        private void NewCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            epub = null;

            paraBodyText.Inlines.Clear();

            button_back.IsEnabled = false;
            button_front.IsEnabled = false;
        }

        // file -> new click
        private void menuNew_Click(object sender, RoutedEventArgs e)
        {
            epub = null;

            paraBodyText.Inlines.Clear();

            button_back.IsEnabled = false;
            button_front.IsEnabled = false;
        }

        // file -> open shortcut(ctrl+o)
        private void OpenCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            fileFullName = ShowFileOpenDialog();
            if (fileFullName != null)
                epub = new Epub(fileFullName);
        }

        // file -> open click
        private void menuOpen_Click(object sender, RoutedEventArgs e)
        {
            fileFullName = ShowFileOpenDialog();
            if (fileFullName != null)
                epub = new Epub(fileFullName);
        }

        // file -> save shortcut(ctrl+s)
        private void SaveCommandBinding_Executed(object sender, ExecutedRoutedEventArgs e)
        {
            ShowFileSaveDialog();
        }

        // file -> save click
        private void menuSave_Click(object sender, RoutedEventArgs e)
        {
            ShowFileOpenDialog();
        }

        private void menuRedo_Click(object sender, RoutedEventArgs e)
        {

        }

        private void menuUndo_Click(object sender, RoutedEventArgs e)
        {

        }

        private void btnPencil_Click(object sender, RoutedEventArgs e)
        {
            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Ink;
        }

        private void btnEraser_Click(object sender, RoutedEventArgs e)
        {
            switch(eraserToUse)
            {
                case "Stroke Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByStroke;
                    break;

                case "Point Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByPoint;
                    break;
            }
        }

        private void eraser_Choice(object sender, RoutedEventArgs e)
        {
            var button = sender as RadioButton;

            eraserToUse = button.Content.ToString();

            switch (eraserToUse)
            {
                case "Stroke Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByStroke;
                    break;

                case "Point Eraser":
                    this.myInkCanvas.EditingMode = InkCanvasEditingMode.EraseByPoint;
                    break;
            }
        }

        private void btnChoice_Click(object sender, RoutedEventArgs e)
        {
            this.myInkCanvas.EditingMode = InkCanvasEditingMode.Select;
        }
    }
}
